export interface Dhikr {
  id: number;
  category: 'morning' | 'evening' | 'sleep' | 'after-prayer' | 'custom' | 'relief' | 'rizq' | 'advice' | 'home' | 'food' | 'mosque' | 'travel' | 'clothing' | 'toilet' | 'surah-baqarah' | 'surah-kahf';
  content: string;
  count: number;
  description: string;
  reference: string;
}
