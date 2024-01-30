import json
import yaml

# чтение данных из JSON файла
with open('in/in.json', 'r', encoding='UTF-8') as json_file:
    json_data = json.load(json_file)

# конвертация в объект Python и последующая конвертация в YAML
yaml_data = yaml.dump(json_data, default_flow_style=False, allow_unicode=True)

# запись данных в файл YAML
with open('out/out.yaml', 'w', encoding='UTF-8') as yaml_file:
    yaml_file.write(yaml_data)

