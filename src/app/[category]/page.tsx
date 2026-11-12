import { notFound } from 'next/navigation';
import { morningAdhkar, eveningAdhkar, sleepAdhkar, afterPrayerAdhkar } from '@/data/adhkar';
import { reliefAdhkar } from '@/data/relief';
import { rizqAdhkar } from '@/data/rizq';
import { adviceAdhkar } from '@/data/advice';
import { homeAdhkar } from '@/data/home';
import { foodAdhkar } from '@/data/food';
import { mosqueAdhkar } from '@/data/mosque';
import { travelAdhkar } from '@/data/travel';
import { clothingAdhkar } from '@/data/clothing';
import { toiletAdhkar } from '@/data/toilet';
import { surahBaqarahAdhkar, surahKahfAdhkar } from '@/data/surahs';
import type { Dhikr } from '@/data/types';
import AdhkarTracker from '@/components/AdhkarTracker';

type Props = {
  params: { category: string };
};

function getAdhkarData(category: string): { title: string; adhkar: Dhikr[] } {
  switch (category) {
    case 'morning':
      return { title: 'أذكار الصباح', adhkar: morningAdhkar };
    case 'evening':
      return { title: 'أذكار المساء', adhkar: eveningAdhkar };
    case 'sleep':
      return { title: 'أذكار النوم', adhkar: sleepAdhkar };
    case 'after-prayer':
      return { title: 'أذكار بعد الصلاة', adhkar: afterPrayerAdhkar };
    case 'relief':
      return { title: 'أدعية الهم والحزن', adhkar: reliefAdhkar };
    case 'rizq':
        return { title: 'أدعية الرزق', adhkar: rizqAdhkar };
    case 'advice':
        return { title: 'وصايا نبوية', adhkar: adviceAdhkar };
    case 'home':
        return { title: 'أذكار المنزل', adhkar: homeAdhkar };
    case 'food':
        return { title: 'أذكار الطعام والشراب', adhkar: foodAdhkar };
    case 'mosque':
        return { title: 'أذكار المسجد', adhkar: mosqueAdhkar };
    case 'travel':
        return { title: 'أذكار الركوب', adhkar: travelAdhkar };
    case 'clothing':
        return { title: 'أذكار لبس الثوب', adhkar: clothingAdhkar };
    case 'toilet':
        return { title: 'أذكار الخلاء', adhkar: toiletAdhkar };
    case 'surah-baqarah':
        return { title: 'سورة البقرة', adhkar: surahBaqarahAdhkar };
    case 'surah-kahf':
        return { title: 'سورة الكهف', adhkar: surahKahfAdhkar };
    default:
      notFound();
  }
}

export async function generateStaticParams() {
  return [
    'morning', 
    'evening', 
    'sleep', 
    'after-prayer', 
    'relief', 
    'rizq', 
    'advice',
    'home',
    'food',
    'mosque',
    'travel',
    'clothing',
    'toilet',
    'surah-baqarah',
    'surah-kahf'
  ].map(category => ({
    category,
  }));
}

export default function AdhkarCategoryPage({ params }: Props) {
  const { category } = params;
  const { title, adhkar } = getAdhkarData(category);

  return <AdhkarTracker title={title} adhkar={adhkar} />;
}
