"""Generate standalone SVG charts using only Python's standard library."""
import csv
from collections import defaultdict
from pathlib import Path
from html import escape

ROOT = Path(__file__).resolve().parents[1]
with (ROOT / 'results/tables/benchmark.csv').open() as source:
    rows = list(csv.DictReader(source))
groups = defaultdict(list)
for row in rows:
    groups[(row['algorithm'], row['graph'])].append(row)
colors = ['#2563eb', '#dc2626', '#0891b2', '#059669', '#9333ea', '#ea580c', '#475569']
output = ROOT / 'results/plots'
output.mkdir(parents=True, exist_ok=True)
for axis, label, filename in [('V', 'Number of vertices (V)', 'time-vs-vertices.svg'),
                              ('E', 'Number of edges (E)', 'time-vs-edges.svg')]:
    xmax = max(int(r[axis]) for r in rows)
    ymax = max(float(r['average_ns']) / 1e6 for r in rows) * 1.12
    x = lambda value: 90 + value / xmax * 720
    y = lambda value: 495 - value / ymax * 380
    svg = ['<svg xmlns="http://www.w3.org/2000/svg" width="1180" height="600" viewBox="0 0 1180 600">',
           '<rect width="1180" height="600" fill="white"/>',
           '<g font-family="Arial,sans-serif" font-size="15" fill="#1e293b">',
           '<text x="90" y="38" font-size="25">Execution time vs. ' + ('vertices' if axis == 'V' else 'edges') + '</text>',
           '<text x="90" y="65">Mean of 5 measured runs after 10 warm-ups; seed 42; linear axes</text>']
    for i in range(6):
        value = ymax * i / 5
        svg += [f'<path d="M90 {y(value):.2f} H810" stroke="#e2e8f0"/>',
                f'<text x="80" y="{y(value)+5:.2f}" text-anchor="end">{value:.3f}</text>']
    svg += ['<path d="M90 115 V495 H810" fill="none" stroke="#334155"/>']
    for value in sorted({int(r[axis]) for r in rows}):
        svg.append(f'<text x="{x(value):.2f}" y="520" text-anchor="middle">{value}</text>')
    svg += [f'<text x="450" y="557" text-anchor="middle">{label}</text>',
            '<text transform="translate(23 310) rotate(-90)" text-anchor="middle">Mean execution time (ms)</text>',
            '<text x="90" y="585" font-size="13">V and E change together; these charts do not isolate their independent effects.</text>']
    for index, ((algorithm, kind), data) in enumerate(groups.items()):
        color = colors[index]
        data.sort(key=lambda r: int(r[axis]))
        points = ' '.join(f"{x(int(r[axis])):.2f},{y(float(r['average_ns'])/1e6):.2f}" for r in data)
        svg.append(f'<polyline points="{points}" fill="none" stroke="{color}" stroke-width="2.5"/>')
        for r in data:
            cx, cy = x(int(r[axis])), y(float(r['average_ns'])/1e6)
            svg.append(f'<circle cx="{cx:.2f}" cy="{cy:.2f}" r="4" fill="{color}"><title>{escape(algorithm)} / {escape(kind)}: {float(r["average_ns"])/1e6:.4f} ms</title></circle>')
        legend_y = 135 + index * 40
        svg += [f'<path d="M850 {legend_y} h24" stroke="{color}" stroke-width="3"/>',
                f'<text x="885" y="{legend_y+5}" font-size="14">{escape(algorithm)} / {escape(kind)}</text>']
    svg += ['</g></svg>']
    target = output / filename
    target.write_text('\n'.join(svg))
    print(target)
