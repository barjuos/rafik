#!/usr/bin/env node
/**
 * Fix adhkar misspellings in both the web sources (src/data/*.ts) and
 * src/data/android_app_data.json.
 *
 * Every replacement is count-verified: if the number of occurrences does not
 * match `expect`, the script aborts without writing anything. Run with
 * `--dry` to only print the counts.
 *
 * Only spelling is touched: no words, sentences, descriptions or entries are
 * added or removed.
 */
import fs from 'node:fs';
import path from 'node:path';

const root = path.resolve(import.meta.dirname, '..');
const dataDir = path.join(root, 'src', 'data');
const dry = process.argv.includes('--dry');

const fixes = [
  // --- corrupted Latin characters inside Arabic words -------------------
  { name: 'stray Latin k in كلمات', find: 'kَلِماتِه', replace: 'كَلِماتِه', json: 1, ts: 1 },
  { name: 'Latin ni in عافني', find: 'عافِni', replace: 'عافِني', json: 5, ts: 0 },
  { name: 'Latin ni in بدني', find: 'بَدَni', replace: 'بَدَني', json: 1, ts: 0 },
  { name: 'Latin si in نفسي', find: 'نَفْsi', replace: 'نَفْسي', json: 3, ts: 0 },

  // --- missing space -----------------------------------------------------
  { name: 'أعوذبك -> أعوذ بك', find: 'أَعوذُبِكَ', replace: 'أَعوذُ بِكَ', json: 4, ts: 4 },

  // --- stray / wrong diacritics -----------------------------------------
  // "وَأَنَّ ُ مُحَمّداً" carries a floating damma between أن and محمد.
  {
    name: 'floating damma after أن',
    find: '\u0648\u064E\u0623\u064E\u0646\u0651\u064E \u064F ',
    replace: '\u0648\u064E\u0623\u064E\u0646\u0651\u064E ',
    json: 2,
    ts: 2,
  },
  { name: 'أَصْبََح (double fatha)', find: 'أَصْبََحَ', replace: 'أَصْبَحَ', json: 1, ts: 1 },
  { name: 'قيُّوم -> قَيُّوم', find: 'قيُّومُ', replace: 'قَيُّومُ', json: 2, ts: 2 },
  { name: 'غُفرَت -> غُفِرَت', find: 'غُفرَتْ', replace: 'غُفِرَتْ', json: 2, ts: 2 },
  {
    name: 'شَيْءِ قَدِيرِ -> شَيْءٍ قَدِيرٌ',
    find: 'شَيْءِ قَدِيرِ',
    replace: 'شَيْءٍ قَدِيرٌ',
    json: 2,
    ts: 2,
  },

  // --- missing hamza / wrong word form ----------------------------------
  { name: 'احد -> أحد (morning-28)', find: 'إلا احد عمل', replace: 'إلا أحد عمل', json: 1, ts: 1 },
  { name: 'وتكن -> وتكون', find: 'وتكن', replace: 'وتكون', json: 1, ts: 1 },
  { name: 'ادركته -> أدركته', find: 'ادركته', replace: 'أدركته', json: 2, ts: 2 },
  {
    name: 'صلى على (upon me) -> صلَّى عليَّ',
    find: 'صلى على حين',
    replace: 'صلَّى عليَّ حين',
    json: 2,
    ts: 2,
  },

  // --- alif maqsura where ya is required --------------------------------
  { name: 'شفاعتى -> شفاعتي', find: 'شفاعتى', replace: 'شفاعتي', json: 2, ts: 2 },
  { name: 'يمسى -> يمسي', find: 'يمسى', replace: 'يمسي', json: 15, ts: 15 },
  { name: 'شىء -> شيء', find: 'شىء', replace: 'شيء', json: 6, ts: 6 },

  // --- hamza / spelling --------------------------------------------------
  { name: 'الأخرة -> الآخرة', find: 'الأخرة', replace: 'الآخرة', json: 2, ts: 2 },
  { name: 'ابن ماجة -> ابن ماجه', find: 'ابن ماجة', replace: 'ابن ماجه', json: 3, ts: 3 },
];

function countOf(text, needle) {
  return text.split(needle).length - 1;
}

const tsFiles = [
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
].map((f) => path.join(dataDir, `${f}.ts`));
const jsonFile = path.join(dataDir, 'android_app_data.json');

const targets = [
  ...tsFiles.map((f) => ({ file: f, kind: 'ts' })),
  { file: jsonFile, kind: 'json' },
];

// Aggregate counts per fix per source kind before touching anything.
const results = [];
for (const fix of fixes) {
  const perKind = { ts: 0, json: 0 };
  for (const target of targets) {
    const text = fs.readFileSync(target.file, 'utf8');
    perKind[target.kind] += countOf(text, fix.find);
  }
  const ok = perKind.ts === fix.ts && perKind.json === fix.json;
  results.push({ fix, perKind, ok });
}

let bad = 0;
for (const { fix, perKind, ok } of results) {
  const label = ok ? 'ok  ' : 'FAIL';
  if (!ok) bad++;
  console.log(
    `${label} ${fix.name}: ts=${perKind.ts}/${fix.ts} json=${perKind.json}/${fix.json}`,
  );
}

if (bad) {
  console.error(`\n${bad} replacement(s) did not match the expected counts. Nothing written.`);
  process.exit(1);
}
if (dry) {
  console.log('\nDry run: all counts match. Nothing written.');
  process.exit(0);
}

// Apply, then verify the counts are zero.
const backups = new Map();
for (const target of targets) {
  backups.set(target.file, fs.readFileSync(target.file, 'utf8'));
}

for (const target of targets) {
  let text = fs.readFileSync(target.file, 'utf8');
  for (const fix of fixes) {
    const expected = target.kind === 'ts' ? fix.ts : fix.json;
    if (expected === 0) continue;
    const before = countOf(text, fix.find);
    if (before === 0) continue;
    text = text.split(fix.find).join(fix.replace);
  }
  fs.writeFileSync(target.file, text, 'utf8');
}

// Verify that every replacement has been fully consumed across each source kind.
for (const fix of fixes) {
  for (const kind of ['ts', 'json']) {
    const expected = kind === 'ts' ? fix.ts : fix.json;
    if (expected === 0) continue;
    let remaining = 0;
    for (const target of targets) {
      if (target.kind !== kind) continue;
      remaining += countOf(fs.readFileSync(target.file, 'utf8'), fix.find);
    }
    if (remaining !== 0) {
      throw new Error(`${fix.name}: ${remaining} occurrence(s) left in ${kind}`);
    }
  }
}

console.log(`\nApplied ${fixes.length} fixes to ${targets.length} files.`);

// Show a UTF-8 diff summary for review.
const changed = [];
for (const target of targets) {
  const before = backups.get(target.file);
  const after = fs.readFileSync(target.file, 'utf8');
  if (before === after) continue;
  const b = before.split(/\r?\n/);
  const a = after.split(/\r?\n/);
  for (let i = 0; i < Math.max(a.length, b.length); i++) {
    if (a[i] !== b[i]) {
      changed.push(`${path.relative(root, target.file)}:${i + 1}`);
      changed.push(`  - ${b[i]}`);
      changed.push(`  + ${a[i]}`);
    }
  }
}
const out = path.join(root, 'scripts', 'adhkar-fixes-diff.txt');
fs.writeFileSync(out, `${changed.join('\n')}\n`, 'utf8');
console.log(`diff: ${path.relative(root, out)} (${changed.length} lines)`);
