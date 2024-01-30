import time
import json
import yaml
import re


def main():
    with open('in/in.json', "r", encoding='UTF-8') as f:
        lines = f.readlines()
        for i in range(len(lines)):
            lines[i] = lines[i].replace("\"", "").replace("{", "").replace("}", "")
        i = 0
        while i < len(lines):
            if '[' in lines[i]:
                lines[i] = lines[i].replace('[', '')
                i += 2
                lines[i] = (len(lines[i]) - len(lines[i].lstrip())) * ' ' + '- ' + lines[i].lstrip()
                while i < len(lines) and ']' not in lines[i]:
                    if lines[i].strip() == ',':
                        i += 2
                        lines[i] = (len(lines[i]) - len(lines[i].lstrip())) * ' ' + '- ' + lines[i].lstrip()
                    i += 1
                lines[i] = lines[i].replace(']', '')
            i += 1
        for i in range(len(lines)):
            lines[i] = lines[i].replace(',', '')
        result_lines = []
        for line in lines:
            if line.strip() != "":
                result_lines.append(line)
        level = -1
        previous_spaces = 0
        for i in range(len(result_lines)):
            spaces = len(result_lines[i]) - len(result_lines[i].lstrip())
            if spaces > previous_spaces:
                level += 1
                previous_spaces = spaces
            elif spaces < previous_spaces:
                level -= 1
                previous_spaces = spaces
            if result_lines[i].lstrip()[0] == '-':
                result_lines[i] = "  " * (level - 1) + result_lines[i].lstrip()
            else:
                result_lines[i] = "  " * level + result_lines[i].lstrip()
    with open('out/out.yaml', 'w', encoding='UTF-8') as f:
        for line in result_lines:
            f.write(line)


def add_1():
    with open('in/in.json', 'r', encoding='UTF-8') as json_file:
        json_data = json.load(json_file)
    yaml_data = yaml.dump(json_data, default_flow_style=False, allow_unicode=True)
    with open('out/out.yaml', 'w', encoding='UTF-8') as yaml_file:
        yaml_file.write(yaml_data)


def add_2():
    with open('in/json.txt', "r", encoding='UTF-8') as f:
        lines = f.readlines()
        for i in range(len(lines)):
            lines[i] = re.sub(r"[\"{}]", "", lines[i])
        i = 0
        while i < len(lines):
            if re.search(r'\[', lines[i]):
                lines[i] = re.sub(r"\[", "", lines[i])
                i += 2
                lines[i] = re.sub(r'^(\s*)', lambda x: len(x[0]) * ' ' + '- ', lines[i])
                while i < len(lines) and not re.search(r'\]', lines[i]):
                    if re.fullmatch(r'^\s*,\s*$', lines[i]):
                        i += 2
                        lines[i] = re.sub(r'^(\s*)', lambda x: len(x[0]) * ' ' + '- ', lines[i])
                    i += 1
                lines[i] = re.sub(r"\]", "", lines[i])
            i += 1
        for i in range(len(lines)):
            lines[i] = re.sub(r",", " ", lines[i])
        result_lines = []
        for line in lines:
            if not re.fullmatch(r'^\s*$', line):
                result_lines.append(line)
        level = -1
        previous_spaces = 0
        for i in range(len(result_lines)):
            match = re.search(r'^(\s*)', result_lines[i])
            spaces = len(match[0]) if match else 0
            if spaces > previous_spaces:
                level += 1
                previous_spaces = spaces
            elif spaces < previous_spaces:
                level -= 1
                previous_spaces = spaces
            if re.fullmatch(r'^\s*-(.*)\n', result_lines[i]):
                result_lines[i] = re.sub(r'^(\s*)', ' ' * (2 * (level - 1)), result_lines[i])
            else:
                result_lines[i] = re.sub(r'^(\s*)', ' ' * (2 * level), result_lines[i])
    with open('out/yaml2.txt', 'w', encoding='UTF-8') as f:
        for line in result_lines:
            f.write(line)


sum_time = 0
for _ in range(100):
    start_time = time.time()  # Начало измерения времени
    main()  # Запуск вашей программы (вызов функции main)
    end_time = time.time()  # Окончание измерения времени
    sum_time += end_time - start_time

print(f"Среднее время выполнения программы для обязательного задания 100 раз: {sum_time} секунд")


sum_time = 0
for _ in range(100):
    start_time = time.time()  # Начало измерения времени
    add_1()  # Запуск вашей программы (вызов функции main)
    end_time = time.time()  # Окончание измерения времени
    sum_time += end_time - start_time

print(f"Среднее время выполнения программы для дополнительного задания №1 100 раз: {sum_time} секунд")


sum_time = 0
for _ in range(100):
    start_time = time.time()  # Начало измерения времени
    add_2()  # Запуск вашей программы (вызов функции main)
    end_time = time.time()  # Окончание измерения времени
    sum_time += end_time - start_time

print(f"Среднее время выполнения программыдля дополнительного задания №2 100 раз: {sum_time} секунд")


