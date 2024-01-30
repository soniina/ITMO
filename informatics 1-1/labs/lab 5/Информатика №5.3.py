import csv
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches

data = {'17/09/18': {}, '15/10/18': {}, '15/11/18': {}, '17/12/18': {}}
labels = []
for date in data:
    labels.append('')
    labels.append(f'        {date}')
    labels.append('')
    labels.append('')
    data[date]['open'] = []
    data[date]['max'] = []
    data[date]['min'] = []
    data[date]['close'] = []

with open('data.csv') as csvfile:
    reader = csv.reader(csvfile)
    next(reader)
    dates, open, close, max, min = [], [], [], [], []
    for row in reader:
        data[row[2]]['open'].append(float(row[4]))
        data[row[2]]['max'].append(float(row[5]))
        data[row[2]]['min'].append(float(row[6]))
        data[row[2]]['close'].append(float(row[7]))

all_data = []
for date in data:
    for column in ['open', 'max', 'min', 'close']:
        all_data.append(data[date][column])

bplot = plt.boxplot(all_data, patch_artist=True, labels=labels)
colors = ['orange', 'yellow', 'lightgreen', 'red'] * 4
for patch, color in zip(bplot['boxes'], colors):
        patch.set_facecolor(color)

legend_patches = [
    mpatches.Patch(color='orange', label='Открытие'),
    mpatches.Patch(color='yellow', label='Макс'),
    mpatches.Patch(color='lightgreen', label='Мин'),
    mpatches.Patch(color='red', label='Закрытие')
]
plt.legend(handles=legend_patches)

plt.tight_layout()
plt.show()
