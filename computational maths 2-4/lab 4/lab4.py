import matplotlib.pyplot as plt
import numpy as np


def plot_all_approximations():
    x_vals = [x for x, y in table]
    y_vals = [y for x, y in table]

    a = min(x_vals)
    b = max(x_vals)
    margin_x = 0.1 * (b - a)
    if a > 0:
        while a - margin_x < 0:
            margin_x /= 2
    margin_y = 0.1 * (max(y_vals) - min(y_vals))
    x_range = np.linspace(a - margin_x, b + margin_x, 1000)

    for name, func, _ in approximations:
        y_range = [func(x) for x in x_range]
        plt.plot(x_range, y_range, label=name)

    plt.scatter(x_vals, y_vals, label="Исходные точки")

    plt.xlabel("x")
    plt.ylabel("y")
    plt.title("Графики аппроксимаций")
    plt.ylim(min(y_vals) - margin_y, max(y_vals) + margin_y)
    plt.legend()
    plt.grid(True, linestyle='--', alpha=0.6)
    plt.tight_layout()
    plt.show()


def print_approximation_result(name, func_view, coeffs, S, R2, std_dev, phi_values, eps_values, func):
    print('══════════════════════════════════════════════════════════════')
    print(f'{name.upper()} АППРОКСИМАЦИЯ: {func_view}')
    print('══════════════════════════════════════════════════════════════')
    print('Коэффициенты:')
    coef_names = ['a', 'b', 'c', 'd']
    for i, coef in enumerate(coeffs):
        label = coef_names[i] if i < len(coef_names) else f'k{i}'
        print(f'  {label} = {coef:.6f}')
    print()

    print(f"{'x_i':>10} {'y_i':>10} {'φ(x_i)':>15} {'ε_i':>15}")
    for i, (x, y) in enumerate(table):
        print(f"{x:10.4f} {y:10.4f} {phi_values[i]:15.6f} {eps_values[i]:15.6f}")

    print(f'\nМера отклонения: S = {S:.6f}')
    print(f'Среднеквадратичное отклонение: δ = {std_dev:.6f}')
    print(f'Коэффициент детерминации: R² = {R2:.6f}')

    if R2 >= 0.95:
        interpretation = "высокая точность аппроксимации"
    elif R2 >= 0.75:
        interpretation = "удовлетворительная аппроксимация"
    elif R2 >= 0.5:
        interpretation = "слабая аппроксимация"
    else:
        interpretation = "недостаточная точность аппроксимации"

    print(f'Интерпретация R²: {interpretation}')

    approximations.append((name, func, std_dev))


def calculate_metrics(func):
    phi_values = [func(x) for x, y in table]
    average_phi = sum(phi_values) / n
    eps_values = [func(x) - y for x, y in table]

    S = sum(eps**2 for eps in eps_values)
    standard_deviation = (S / n) ** 0.5
    R2 = 1 - S / sum((y - average_phi) ** 2 for x, y in table)

    return func, S, R2, standard_deviation, phi_values, eps_values


def linear_regression(x_values, y_values):
    sx = sum(x_values)
    sxx = sum(x**2 for x in x_values)
    sy = sum(y_values)
    sxy = sum(x * y for x, y in zip(x_values, y_values))

    delta = sxx * n - sx * sx
    delta1 = sxy * n - sx * sy
    delta2 = sxx * sy - sx * sxy

    a = delta1 / delta
    b = delta2 / delta
    return a, b


def linear_approximation():
    average_x = sum(list(x for x, y in table)) / n
    average_y = sum(list(y for x, y in table)) / n

    r = (sum((x - average_x) * (y - average_y) for x, y in table) /
         (sum((x - average_x)**2 for x, y in table) * sum((y - average_y)**2 for x, y in table))**0.5)

    a, b = linear_regression([x for x, y in table], [y for x, y in table])
    P1 = lambda x: a*x + b

    return (a, b), *calculate_metrics(P1), r


def quadratic_approximation():
    sx = sum(list(x for x, y in table))
    sxx = sum(list(x**2 for x, y in table))
    sxxx = sum(list(x**3 for x, y in table))
    sxxxx = sum(list(x**4 for x, y in table))
    sy = sum(list(y for x, y in table))
    sxy = sum(list(x * y for x, y in table))
    sxxy = sum(list(x**2 * y for x, y in table))

    A = [[sxxxx, sxxx, sxx],
         [sxxx, sxx, sx],
         [sxx, sx, n]]
    B = [sxxy, sxy, sy]

    a, b, c = np.linalg.solve(np.array(A, dtype=float), np.array(B, dtype=float))
    P2 = lambda x: c + b*x + a*x**2

    return (a, b, c), *calculate_metrics(P2)


def cubic_approximation():
    sx = sum(list(x for x, y in table))
    sxx = sum(list(x**2 for x, y in table))
    sxxx = sum(list(x**3 for x, y in table))
    sxxxx = sum(list(x**4 for x, y in table))
    sxxxxx = sum(list(x**5 for x, y in table))
    sxxxxxx = sum(list(x**6 for x, y in table))
    sy = sum(list(y for x, y in table))
    sxy = sum(list(x * y for x, y in table))
    sxxy = sum(list(x**2 * y for x, y in table))
    sxxxy = sum(list(x**3 * y for x, y in table))

    A = [[sxxxxxx, sxxxxx, sxxxx, sxxx],
         [sxxxxx, sxxxx, sxxx, sxx],
         [sxxxx, sxxx, sxx, sx],
         [sxxx, sxx, sx, n]]
    B = [sxxxy, sxxy, sxy, sy]

    a, b, c, d = np.linalg.solve(np.array(A, dtype=float), np.array(B, dtype=float))
    P3 = lambda x: a*x**3 + b*x**2 + c*x + d

    return (a, b, c, d), *calculate_metrics(P3)


def power_approximation():
    X = [np.log(x) for x, y in table]
    Y = [np.log(y) for x, y in table]
    A, B = linear_regression(X, Y)

    a = np.exp(B)
    b = A
    P4 = lambda x: a * x**b

    return (a, b), *calculate_metrics(P4)


def exponential_approximation():
    X = [x for x, y in table]
    Y = [np.log(y) for x, y in table]
    A, B = linear_regression(X, Y)

    a = np.exp(B)
    b = A
    P5 = lambda x: a * np.exp(b*x)

    return (a, b), *calculate_metrics(P5)


def logarithmic_approximation():
    X = [np.log(x) for x, y in table]
    Y = [y for x, y in table]
    a, b = linear_regression(X, Y)

    P6 = lambda x: a*np.log(x) + b

    return (a, b), *calculate_metrics(P6)


# --- Ввод данных ---

n = 0
table = []
approximations = []

print('Вычислительная математика. Лабораторная работа 4: "Аппроксимация функции методом наименьших квадратов".\n')

print("Как вы хотите ввести исходные данные?\n1 - с консоли\n2 - с файла")
v = input()
while v not in {'1', '2'}:
    print('Выберите первый (1) или второй (2) вариант')
    v = input()

print()
if v == '1':
    n = int(input("Сколько точек вы хотите ввести (от 8 до 12)? "))
    while not n in range(8, 13):
        n = int(input("Выберите значение от 8 до 12: "))
    print("Введите построчно значения x и y через пробел:")
    for i in range(n):
        table.append(tuple(map(float, input().split())))
else:
    print("Введите название файла:")
    with open(input().strip(), 'r') as file:
        lines = file.readlines()
        n = int(lines[0])
        for i in range(n):
            table.append(tuple(map(float, lines[i + 1].split())))
print()


# --- Линейное приближение ---

(a, b), func, S, R2, std_dev, phi_values, eps_values, r = linear_approximation()

print_approximation_result(
    "Линейная",
    "y = ax + b",
    (a, b),
    S, R2, std_dev,
    phi_values, eps_values,
    func
)
print(f'Коэффициент корреляции Пирсона: r = {r:.6f}')

if abs(r) == 1:
    interpretation = "строгая линейная функциональная зависимость"
elif r >= 0.9:
    interpretation = "связь весьма высокая"
elif r >= 0.7:
    interpretation = "связь высокая"
elif r >= 0.5:
    interpretation = "связь заметная"
elif r >= 0.3:
    interpretation = "связь умеренная"
elif r > 0:
    interpretation = "связь слабая"
else:
    interpretation = "связь отсутствует"

print(f'Интерпретация коэффициента корреляции Пирсона: {interpretation}\n\n')


# --- Квадратичное приближение ---

(a, b, c), func, S, R2, std_dev, phi_values, eps_values = quadratic_approximation()
print_approximation_result(
    "Квадратичная",
    "y = ax^2 + bx + c",
    (a, b, c),
    S, R2, std_dev,
    phi_values, eps_values,
    func
)
print('\n\n')


# --- Кубическое приближение ---

(a, b, c, d), func, S, R2, std_dev, phi_values, eps_values = cubic_approximation()
print_approximation_result(
    "Кубическая",
    "y = ax^3 + bx^2 + cx + d",
    (a, b, c, d),
    S, R2, std_dev,
    phi_values, eps_values,
    func
)
print('\n\n')


# --- Степенное приближение ---

if any(x <= 0 or y <= 0 for x, y in table):
    print("Для степенной аппроксимации все x и y должны быть > 0\n\n")
else:
    (a, b), func, S, R2, std_dev, phi_values, eps_values = power_approximation()
    print_approximation_result(
        "Степенная",
        "y = a * x^b",
        (a, b),
        S, R2, std_dev,
        phi_values, eps_values,
        func
    )
    print('\n\n')


# --- Экспоненциальное приближение ---

if any(y <= 0 for x, y in table):
    print("Для экспоненциальной аппроксимации все y должны быть > 0\n\n")
else:
    (a, b), func, S, R2, std_dev, phi_values, eps_values = exponential_approximation()
    print_approximation_result(
        "Экспоненциальная",
        "y = a * e^(bx)",
        (a, b),
        S, R2, std_dev,
        phi_values, eps_values,
        func
    )
    print('\n\n')


# --- Логарифмическое приближение ---

if any(x <= 0 for x, y in table):
    print("Для логарифмической аппроксимации все x должны быть > 0\n\n")
else:
    (a, b), func, S, R2, std_dev, phi_values, eps_values = logarithmic_approximation()
    print_approximation_result(
        "Логарифмическая",
        "y = a * ln(x) + b",
        (a, b),
        S, R2, std_dev,
        phi_values, eps_values,
        func
    )
    print('\n\n')


plot_all_approximations()

# --- Лучшие приближения ---

min_std_dev = min(std for _, _, std in approximations)
best_approximations = [(name, std) for name, _, std in approximations if abs(std - min_std_dev) < 1e-8]

print('══════════════════════════════════════════════════════════════')
print('ЛУЧШАЯ(-ИЕ) АППРОКСИМАЦИЯ(-И):')
for name, std in best_approximations:
    print(f'- {name.upper()} (δ = {std:.6f})')
print('══════════════════════════════════════════════════════════════')
