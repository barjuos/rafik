'use client';

import { useState, useEffect, useMemo, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Progress } from '@/components/ui/progress';
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card';
import { CheckCircle2 } from 'lucide-react';
import type { Dhikr } from '@/data/types';

interface AdhkarTrackerProps {
  title: string;
  adhkar: Dhikr[];
}

export default function AdhkarTracker({ title, adhkar }: AdhkarTrackerProps) {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [currentCount, setCurrentCount] = useState(0);
  const [isCompleted, setIsCompleted] = useState(false);
  const completionAudioRef = useRef<HTMLAudioElement | null>(null);
  const singleTickAudioRef = useRef<HTMLAudioElement | null>(null);


  useEffect(() => {
    if (typeof window !== 'undefined') {
      completionAudioRef.current = new Audio('/completion-sound.mp3');
      completionAudioRef.current.load();
      singleTickAudioRef.current = new Audio('/single-completion.mp3');
      singleTickAudioRef.current.load();
    }
  }, []);

  const currentDhikr = useMemo(() => adhkar[currentIndex], [adhkar, currentIndex]);

  const handleTap = () => {
    if (isCompleted || !currentDhikr) return;

    if (currentCount + 1 < currentDhikr.count) {
        setCurrentCount(currentCount + 1);
    } else {
      const isLastDhikr = currentIndex === adhkar.length - 1;

      if (isLastDhikr) {
        if (completionAudioRef.current) {
          completionAudioRef.current.play().catch(e => console.error("Error playing final completion sound:", e));
        }
        if (typeof window !== 'undefined' && window.navigator?.vibrate) {
          window.navigator.vibrate([200, 100, 200]);
        }
        setIsCompleted(true);
      } else {
        if (singleTickAudioRef.current) {
           // On mobile, it's often safer to load() before play()
           singleTickAudioRef.current.load();
           singleTickAudioRef.current.play().catch(e => console.error("Error playing tick sound:", e));
        }
        if (typeof window !== 'undefined' && window.navigator?.vibrate) {
          window.navigator.vibrate(100);
        }
        
        setTimeout(() => {
            setCurrentIndex(currentIndex + 1);
            setCurrentCount(0);
        }, 250);
      }
    }
  };

  if (isCompleted) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[80vh] text-center px-4">
        <motion.div
          initial={{ scale: 0.5, opacity: 0 }}
          animate={{ scale: 1, opacity: 1 }}
          transition={{ duration: 0.5, type: 'spring' }}
        >
          <CheckCircle2 className="h-24 w-24 text-primary mx-auto mb-6" />
          <h2 className="text-3xl font-headline font-bold text-foreground mb-2">تقبل الله</h2>
          <p className="text-muted-foreground">لقد أتممت {title}.</p>
        </motion.div>
      </div>
    );
  }

  if (!currentDhikr) {
     return (
        <div className="flex flex-col items-center justify-center min-h-[80vh] text-center px-4">
             <h2 className="text-3xl font-headline font-bold text-foreground mb-2">لا يوجد أذكار</h2>
             <p className="text-muted-foreground">لم يتم العثور على أذكار في هذه القائمة.</p>
        </div>
     );
  }

  const progress = ((currentIndex) / adhkar.length) * 100;
  
  return (
    <div className="flex flex-col h-[calc(100vh-80px)]">
      <div className="px-4 pt-2">
         <p className="text-muted-foreground text-sm mb-2">{title} - {currentIndex + 1} من {adhkar.length}</p>
        <Progress value={progress} className="w-full bg-primary/20" />
      </div>

      <div
        className="flex-1 flex items-center justify-center p-4"
        onClick={handleTap}
        role="button"
        tabIndex={0}
        aria-label={`اضغط لزيادة عداد الذكر. الذكر الحالي: ${currentDhikr.content}`}
      >
        <AnimatePresence mode="wait">
          <motion.div
            key={currentIndex}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -20 }}
            transition={{ duration: 0.3 }}
            className="w-full max-w-2xl"
          >
            <Card className="text-center shadow-lg select-none cursor-pointer bg-card/90 backdrop-blur-sm overflow-hidden border-primary/20">
                <CardHeader className="pt-8">
                    <div className="relative w-32 h-32 mx-auto mb-6">
                        <svg viewBox="0 0 36 36" className="absolute inset-0 w-full h-full">
                            <circle cx="18" cy="18" r="15.9155" className="stroke-current text-primary/20" strokeWidth="2.5" fill="none" />
                        </svg>
                        <motion.svg
                          viewBox="0 0 36 36"
                          className="absolute inset-0 w-full h-full"
                          initial={{ strokeDashoffset: 100 }}
                           animate={{ strokeDashoffset: (100) * (1 - (currentCount / (currentDhikr.count || 1))) }}
                          transition={{ duration: 0.5, ease: "linear" }}
                        >
                            <circle
                                cx="18" cy="18" r="15.9155"
                                className="stroke-current text-accent"
                                strokeWidth="2.5" fill="none"
                                strokeDasharray="100"
                                transform="rotate(-90 18 18)"
                                style={{strokeLinecap: 'round'}}
                            />
                        </motion.svg>
                        <div className="absolute inset-0 flex items-center justify-center">
                            <span className="text-4xl font-bold text-foreground">{currentCount}</span>
                            <span className="text-lg text-muted-foreground">/{currentDhikr.count}</span>
                        </div>
                    </div>
                </CardHeader>
              <CardContent className="px-6 pb-4">
                <p className="text-3xl md:text-4xl font-semibold text-foreground/90 text-center" style={{ lineHeight: '3rem' }}>
                  {currentDhikr.content}
                </p>
              </CardContent>
              {(currentDhikr.description || currentDhikr.reference) && (
                <CardFooter className="flex flex-col gap-2 text-sm text-muted-foreground px-6 pb-6 bg-secondary/80 pt-4">
                    {currentDhikr.description && <p className="text-center">{currentDhikr.description}</p>}
                    {currentDhikr.reference && <p className="font-bold text-center">{currentDhikr.reference}</p>}
                </CardFooter>
              )}
            </Card>
          </motion.div>
        </AnimatePresence>
      </div>
    </div>
  );
}
