#!/usr/bin/env node
/**
 * Export the web app's adhkar data to the Android asset format.
 *
 * The web app stores its content in src/data/*.ts. That data was already
 * mirrored into src/data/android_app_data.json (category -> items). This
 * script:
 *   1. flattens that JSON into the array shape the Android app consumes,
 *   2. cross-checks every (category, id) pair against the TypeScript files,
 *   3. validates required fields,
 *   4. writes android/app/src/main/assets/adhkar.json.
 *
 * Run with: npm run android:data
 */
import fs from 'node:fs';
import path from 'node:path';

const root = path.resolve(import.meta.dirname, '..');

const sourceFile = path.join(root, 'src', 'data', 'android_app_data.json');
const source = JSON.parse(fs.readFileSync(sourceFile, 'utf8'));

const entries = Object.entries(source).flatMap(([category, items]) =>
  items.map((item) => ({
    id: item.id,
    category,
    content: item.content ?? '',
    count: item.count,
    description: item.description ?? '',
    reference: item.reference ?? '',
  })),
);

// Cross-check against the TypeScript source of truth.
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
];

const tsKeys = new Set();
for (const name of tsFiles) {
  const text = fs.readFileSync(path.join(root, 'src', 'data', `${name}.ts`), 'utf8');
  for (const match of text.matchAll(/id:\s*(\d+),\s*category:\s*'([^']+)'/g)) {
    tsKeys.add(`${match[2]}-${match[1]}`);
  }
}

const jsonKeys = new Set(entries.map((entry) => `${entry.category}-${entry.id}`));
const missing = [...tsKeys].filter((key) => !jsonKeys.has(key));
const extra = [...jsonKeys].filter((key) => !tsKeys.has(key));

if (tsKeys.size === 0) {
  console.error('No adhkar IDs were found in src/data/*.ts — aborting.');
  process.exit(1);
}
if (missing.length || extra.length) {
  console.error('Data mismatch between TypeScript sources and android_app_data.json.');
  if (missing.length) console.error('Missing from JSON:', missing);
  if (extra.length) console.error('Extra in JSON:', extra);
  process.exit(1);
}

const invalid = entries.filter(
  (entry) =>
    !Number.isInteger(entry.id) ||
    typeof entry.content !== 'string' ||
    entry.content.trim().length === 0 ||
    !Number.isInteger(entry.count) ||
    entry.count < 1,
);
if (invalid.length) {
  console.error('Invalid entries:', invalid);
  process.exit(1);
}

const outDir = path.join(root, 'android', 'app', 'src', 'main', 'assets');
fs.mkdirSync(outDir, { recursive: true });
const outFile = path.join(outDir, 'adhkar.json');
fs.writeFileSync(outFile, `${JSON.stringify(entries, null, 2)}\n`, 'utf8');

const categories = new Set(entries.map((entry) => entry.category));
console.log(
  `Exported ${entries.length} adhkar across ${categories.size} categories -> ${path.relative(root, outFile)}`,
);
