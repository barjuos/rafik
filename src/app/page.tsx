import Link from 'next/link';
import { Card, CardHeader, CardTitle } from '@/components/ui/card';
import { Sunrise, Sunset, Moon, HandPlatter, ListPlus, Smile, Sprout, Star, Home as HomeIcon, Utensils, Landmark, Car, Shirt, Bath, BookOpen } from 'lucide-react';
import { Header } from '@/components/Header';

const categories = [
  { name: 'أذكار الصباح', href: '/morning', icon: <Sunrise className="h-10 w-10" /> },
  { name: 'أذكار المساء', href: '/evening', icon: <Sunset className="h-10 w-10" /> },
  { name: 'أذكار النوم', href: '/sleep', icon: <Moon className="h-10 w-10" /> },
  { name: 'أذكار بعد الصلاة', href: '/after-prayer', icon: <HandPlatter className="h-10 w-10" /> },
  { name: 'دخول/خروج المنزل', href: '/home', icon: <HomeIcon className="h-10 w-10" /> },
  { name: 'الطعام والشراب', href: '/food', icon: <Utensils className="h-10 w-10" /> },
  { name: 'دخول/خروج المسجد', href: '/mosque', icon: <Landmark className="h-10 w-10" /> },
  { name: 'الركوب', href: '/travel', icon: <Car className="h-10 w-10" /> },
  { name: 'لبس الثوب', href: '/clothing', icon: <Shirt className="h-10 w-10" /> },
  { name: 'دخول/خروج الخلاء', href: '/toilet', icon: <Bath className="h-10 w-10" /> },
  { name: 'أدعية الهم والحزن', href: '/relief', icon: <Smile className="h-10 w-10" /> },
  { name: 'أدعية الرزق', href: '/rizq', icon: <Sprout className="h-10 w-10" /> },
  { name: 'وصايا نبوية', href: '/advice', icon: <Star className="h-10 w-10" /> },
  { name: 'سورة البقرة', href: '/surah-baqarah', icon: <BookOpen className="h-10 w-10" /> },
  { name: 'سورة الكهف', href: '/surah-kahf', icon: <BookOpen className="h-10 w-10" /> },
  { name: 'أذكاري', href: '/my-adhkar', icon: <ListPlus className="h-10 w-10" /> },
];

export default function Home() {
  return (
    <div className="flex flex-col min-h-screen bg-background">
      <Header />
      <main className="flex-1 container mx-auto px-4 py-8">
        <div className="text-center mb-12">
          <h1 className="text-4xl md:text-5xl font-headline font-bold text-foreground mb-4">
            رفيق
          </h1>
          <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
            تطبيقك اليومي للأذكار، ليطمئن قلبك بذكر الله.
          </p>
        </div>
        
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {categories.map((category) => (
            <Link
              href={category.href}
              key={category.name}
              className="block transform hover:-translate-y-1 transition-transform duration-300"
            >
              <Card className="h-full flex flex-col items-center justify-center text-center p-6 hover:bg-primary/10 transition-colors duration-300">
                <CardHeader>
                  <div className="mx-auto text-primary mb-4">
                    {category.icon}
                  </div>
                  <CardTitle className="font-headline text-2xl text-foreground">
                    {category.name}
                  </CardTitle>
                </CardHeader>
              </Card>
            </Link>
          ))}
        </div>
      </main>
      <footer className="text-center p-4 text-muted-foreground text-sm">
        <p>صنع بحب ❤️</p>
      </footer>
    </div>
  );
}
