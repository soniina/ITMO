import re

with open('in/json.txt', "r", encoding='UTF-8') as f:
    lines = f.readlines()
    for i in range(len(lines)):
        lines[i] = re.sub(r"[\"{}]", "", lines[i])
    i = 0
    while i < len(lines):
        if '[' in lines[i]:
            lines[i] = re.sub(r"\[", "", lines[i])
            i += 2
            # x - объект match, x[0] - первая подстрока, соответсвующая шаблону
            lines[i] = re.sub(r'^(\s*)', lambda x: len(x[0]) * ' ' + '- ', lines[i])
            while i < len(lines) and not re.search(r'\]', lines[i]):
                if re.fullmatch(r'^\s*,\s*$', lines[i]):
                    i += 2
                    lines[i] = re.sub(r'^(\s*)', lambda x: len(x[0]) * ' ' + '- ',
                                      lines[i])
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
            result_lines[i] = re.sub(r'^(\s*)', ' ' * (2 * (level - 1)),
                                     result_lines[i])
        else:
            result_lines[i] = re.sub(r'^(\s*)', ' ' * (2 * level), result_lines[i])

with open('out/yaml2.txt', 'w', encoding='UTF-8') as f:
    for line in result_lines:
        f.write(line)

