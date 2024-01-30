import pandas as pd
import json

with open('in/in.json', 'r', encoding='utf-8') as json_file:
    data = json.load(json_file)

df = pd.DataFrame(data)

df.to_csv('out/out.tsv', sep='\t', index=False)

