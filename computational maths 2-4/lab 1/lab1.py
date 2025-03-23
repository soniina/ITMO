import copy
import numpy as np


def print_matrix(matrix, title):
    """Печатает матрицу с заголовком и форматированием до 3 знаков после запятой."""
    print(title)
    for row in matrix:
        print("  ".join(f"{elem:.3f}" for elem in row))
    print()


def to_upper_triangular(matrix, n):
    """Прямой ход метода Гаусса — приведение к верхнетреугольному виду"""
    triangular_matrix = copy.deepcopy(matrix)  # Создаём копию, чтобы не изменять исходную матрицу
    det = 1  # Определитель
    swap_count = 0  # Количество перестановок строк

    for i in range(n):
        # Выбор главного элемента по столбцу (поиск максимума по модулю)
        pivot_row = i
        for row in range(i + 1, n):
            if abs(triangular_matrix[row][i]) > abs(triangular_matrix[pivot_row][i]):
                pivot_row = row

        # Меняем строки, если найден другой главный элемент
        if pivot_row != i:
            triangular_matrix[i], triangular_matrix[pivot_row] = triangular_matrix[pivot_row], triangular_matrix[i]
            swap_count += 1

        # Если найден нулевой элемент на диагонали — система вырожденная
        if triangular_matrix[i][i] == 0:
            return triangular_matrix, 0

        det *= triangular_matrix[i][i]

        # Зануление элементов под главной диагональю
        for elimination_row in range(i + 1, n):
            k = triangular_matrix[elimination_row][i] / triangular_matrix[i][i]
            for col in range(i, n + 1):
                triangular_matrix[elimination_row][col] -= k * triangular_matrix[i][col]

    # Корректируем знак определителя в зависимости от количества перестановок
    return triangular_matrix, det * (-1) ** swap_count


def solve_slae(triangular_matrix, n):
    """Обратный ход метода Гаусса - находим переменные"""
    solution = [0] * n
    for i in range(n - 1, -1, -1):
        # Находим значение x_i
        solution[i] = triangular_matrix[i][n] / triangular_matrix[i][i]

        # Вычитаем найденное значение из предыдущих уравнений
        for j in range(i - 1, -1, -1):
            triangular_matrix[j][n] -= triangular_matrix[j][i] * solution[i]

    return solution


def compute_residual(matrix, n, solution):
    """Вычисляет вектор невязки: разницу между Ax и b."""
    residual = [0] * n
    for i in range(n):
        Ax_i = sum(matrix[i][j] * solution[j] for j in range(n))
        residual[i] = Ax_i - matrix[i][n]

    return residual


# --- Ввод данных ---

n = 0
matrix = []

print('Вычислительная математика. Лабораторная работа 1: "Решение системы линейных алгебраических уравнений СЛАУ".\n'
      'Вариант 13: "Метод Гаусса с выбором главного элемента по столбцам"\n')

print("Как вы хотите задать коэффициенты матрицы?\n1 - с консоли\n2 - с файла")
v = input()
while v not in {'1', '2'}:
    print('Выберите первый (1) или второй (2) вариант')
    v = input()

print()
if v == '1':
    print("Введите размерность матрицы:")
    n = int(input())
    print("Введите коэффициенты матрицы (каждая строка через пробел):")
    for i in range(n):
        row = list(map(float, input().split()))
        matrix.append(row)
else:
    print("Введите название файла:")
    with open(input().strip(), 'r') as file:
        lines = file.readlines()
        n = int(lines[0])
        for i in range(n):
            row = list(map(float, lines[i + 1].split()))
            matrix.append(row)
print()


# --- Решение СЛАУ ---

print_matrix(matrix, "Исходная матрица:")

triangular_matrix, determinant = to_upper_triangular(matrix, n)
print_matrix(triangular_matrix, "Треугольная матрица:")

print(f"Определитель матрицы: {determinant:.3f}")

if determinant == 0:
    print("Система несовместна или имеет бесконечно много решений.")
else:
    solution = solve_slae(triangular_matrix, n)
    print("Решение системы:" + ", ".join(f"x{i+1} = {x:.3f}" for i, x in enumerate(solution)))

    residual = compute_residual(matrix, n, solution)
    print("Вектор невязки:" + ", ".join(f"r{i+1} = {r:.3e}" for i, r in enumerate(residual)))

print("\n\nПроверим результаты, используя библиотеки:\n")

A = np.array([row[:-1] for row in matrix], dtype=float)
b = np.array([row[-1] for row in matrix], dtype=float)

print(f"Определитель матрицы (NumPy): {np.linalg.det(A):.3f}")
print("Решение системы (NumPy):", ", ".join(f"x{i+1} = {xi:.3f}" for i, xi in enumerate(np.linalg.solve(A, b))))
