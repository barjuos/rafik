#!/usr/bin/env node
/**
 * Read-only audit of the adhkar text.
 *
 * 1. Parses every src/data/*.ts array and compares it field-by-field with
 *    src/data/android_app_data.json (what the Android app ships).
 * 2. Scans all text for characters that should not be there (tatweel,
 *    zero-width marks, presentation forms, Latin letters, double spaces...).
 * 3. Flags suspicious repeats and a list of known Arabic misspellings.
 *
 * Writes a UTF-8 report to scripts/adhkar-audit-report.md
 */
import fs from 'node:fs';
import path from 'node:path';

const root = path.resolve(import.meta.dirname, '..');
const dataDir = path.join(root, 'src', 'data');

const files = [
  'adhkar',
  'relief',
  'rizq',
  'advice',
  'home',
  'food',
  'mosque',
  'travel',
  'clothing',
  'toilet',
];

/** Pull every `export const x: Dhikr[] = [...]` array out of a TS file. */
function parseTs(file) {
  const text = fs.readFileSync(path.join(dataDir, `${file}.ts`), 'utf8');
  const out = [];
  const re = /export\s+const\s+(\w+)\s*:\s*Dhikr\[\]\s*=\s*(\[[\s\S]*?\]);/g;
  let match;
  while ((match = re.exec(text))) {
    // The array body is plain JS/JSON, so eval is safe here.
    const arr = new Function(`return ${match[2]};`)();
    out.push(...arr);
  }
  return out;
}

const tsEntries = files.flatMap(parseTs);
const json = JSON.parse(fs.readFileSync(path.join(dataDir, 'android_app_data.json'), 'utf8'));
const jsonEntries = Object.entries(json).flatMap(([category, items]) =>
  items.map((item) => ({ ...item, category })),
);

const key = (e) => `${e.category}-${e.id}`;
const tsMap = new Map(tsEntries.map((e) => [key(e), e]));
const jsonMap = new Map(jsonEntries.map((e) => [key(e), e]));

const lines = [];
const say = (s = '') => lines.push(s);

say('# Adhkar text audit');
say();
say(`- TypeScript entries: ${tsEntries.length}`);
say(`- android_app_data.json entries: ${jsonEntries.length}`);
say(`- Categories: ${new Set(tsEntries.map((e) => e.category)).size}`);
say();

// ---------------------------------------------------------------- drift
say('## 1. TS vs android_app_data.json');
say();
let driftCount = 0;
for (const k of tsMap.keys()) {
  if (!jsonMap.has(k)) {
    say(`- MISSING in json: ${k}`);
    driftCount++;
  }
}
for (const k of jsonMap.keys()) {
  if (!tsMap.has(k)) {
    say(`- EXTRA in json: ${k}`);
    driftCount++;
  }
}
for (const [k, ts] of tsMap) {
  const js = jsonMap.get(k);
  if (!js) continue;
  for (const field of ['content', 'description', 'reference', 'count']) {
    if (ts[field] !== js[field]) {
      driftCount++;
      say(`- DIFF ${k} [${field}]`);
      say(`  - ts  : ${JSON.stringify(ts[field])}`);
      say(`  - json: ${JSON.stringify(js[field])}`);
    }
  }
}
say();
say(driftCount === 0 ? 'No drift: the Android asset is identical to the web source.' : `${driftCount} difference(s).`);
say();

// ---------------------------------------------------------------- characters
say('## 2. Character problems');
say();
const ARABIC = /[\u0600-\u06FF\u0750-\u077F\uFB50-\uFDFF\uFE70-\uFEFF]/;
const checks = [
  ['tatweel (ـ)', /\u0640/],
  ['zero width (ZWJ/ZWNJ/BOM)', /[\u200B-\u200F\uFEFF]/],
  ['presentation form', /[\uFB50-\uFDFF\uFE70-\uFEFF]/],
  ['latin letter', /[A-Za-z]/],
  ['double space', / {2,}/],
  ['leading/trailing space', /^\s|\s$/],
  ['arabic-indic digit', /[\u0660-\u0669]/],
  ['non-standard space (nbsp)', /\u00A0/],
  ['repeated word', /\b(\S+)\s+\1\b/],
];
let charIssues = 0;
for (const e of tsEntries) {
  for (const field of ['content', 'description', 'reference']) {
    const value = e[field] ?? '';
    for (const [label, re] of checks) {
      const m = value.match(re);
      if (m) {
        charIssues++;
        say(`- ${key(e)} ${field}: ${label} -> ${JSON.stringify(m[0])}`);
      }
    }
    if (value && !ARABIC.test(value)) {
      charIssues++;
      say(`- ${key(e)} ${field}: no Arabic characters at all`);
    }
  }
}
say();
say(charIssues === 0 ? 'No character problems found.' : `${charIssues} character issue(s).`);
say();

// ---------------------------------------------------------------- known typos
say('## 3. Known misspelling patterns');
say();
const misspellings = [
  [/إنشاء\s*الله/g, 'إن شاء الله (spaced)'],
  [/ان\s*شاء\s*الله/g, 'إن شاء الله (hamza)'],
  [/الرحمان/g, 'الرحمن'],
  [/الإستغفار/g, 'الاستغفار'],
  [/إستغفار/g, 'استغفار'],
  [/إستغفر/g, 'استغفر'],
  [/لا\s+اله\s+الا/g, 'لا إله إلا'],
  [/اللهم\s+صلي/g, 'اللهم صلِّ'],
  [/صلى\s+الله\s+علية/g, 'صلى الله عليه'],
  [/عبد\s+الله/g, 'عبدالله (usually joined)'],
  [/إن\s+شأ\s+الله/g, 'إن شاء الله'],
  [/أذن\s+الله/g, 'أذِن الله'],
  [/اللهم\s+أغفر/g, 'اللهم اغفر'],
  [/أستغفر\s+الله\s+وأتوب\s+اليه/g, 'وأتوب إليه'],
  [/المغفرة\s+والرحمه/g, 'والرحمة'],
  [/تبارك\s+وتعالي/g, 'تبارك وتعالى'],
];
let typoIssues = 0;
for (const e of tsEntries) {
  for (const field of ['content', 'description', 'reference']) {
    const value = e[field] ?? '';
    for (const [re, hint] of misspellings) {
      const m = value.match(re);
      if (m) {
        typoIssues++;
        say(`- ${key(e)} ${field}: "${m[0]}" -> ${hint}`);
      }
    }
  }
}
say();
say(typoIssues === 0 ? 'No known misspelling pattern matched.' : `${typoIssues} possible misspelling(s).`);
say();

// ---------------------------------------------------------------- vocabulary
say('## 4. Frequency list of words (for manual review)');
say();
const freq = new Map();
for (const e of tsEntries) {
  for (const field of ['content', 'description', 'reference']) {
    const words = (e[field] ?? '')
      .replace(/[\u064B-\u0652\u0670\u0640]/g, '') // strip diacritics for counting
      .split(/[^\u0621-\u064A\u0671]+/)
      .filter((w) => w.length > 2);
    for (const w of words) freq.set(w, (freq.get(w) ?? 0) + 1);
  }
}
const rare = [...freq.entries()].filter(([, n]) => n === 1).map(([w]) => w).sort();
say(`Unique words: ${freq.size}; appearing once: ${rare.length}`);
say();
say('Words used exactly once (typos usually hide here):');
say();
say(rare.join(' · '));
say();

// ---------------------------------------------------------------- dump
say('## 5. Full text dump');
say();
for (const e of tsEntries) {
  say(`### ${key(e)} (count ${e.count})`);
  say(`- content: ${e.content}`);
  if (e.description) say(`- description: ${e.description}`);
  if (e.reference) say(`- reference: ${e.reference}`);
  say();
}

const out = path.join(root, 'scripts', 'adhkar-audit-report.md');
fs.writeFileSync(out, `${lines.join('\n')}\n`, 'utf8');
console.log(`report: ${path.relative(root, out)}`);
console.log(`drift=${driftCount} chars=${charIssues} typos=${typoIssues}`);
