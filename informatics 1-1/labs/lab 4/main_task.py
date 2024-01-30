with open('in/json.txt', "r", encoding='UTF-8') as f:
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


with open('out/yaml.txt', 'w', encoding='UTF-8') as f:
    for line in result_lines:
        f.write(line)



