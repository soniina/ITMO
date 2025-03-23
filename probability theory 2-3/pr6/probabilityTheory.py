import math

import matplotlib.pyplot as plt


def variational_series(data):
    return sorted(data)


def extreme_values(data):
    return min(data), max(data)


def span(data):
    return max(data) - min(data)


def median(data):
    n = len(data)
    if n % 2 == 1:
        return data[n // 2]
    else:
        mid1, mid2 = data[(n // 2) - 1], data[n // 2]
        return (mid1 + mid2) / 2


def mode(data):
    frequency = {}
    for x in data:
        frequency[x] = frequency.get(x, 0) + 1
    max_freq = max(frequency.values())
    modes = [key for key, value in frequency.items() if value == max_freq]
    return modes


def mean(data):
    total = 0
    for x in data:
        total += x
    return total / len(data)


def sample_variance(data):
    m = mean(data)
    total = 0
    for x in data:
        total += (x - m) ** 2
    return total / len(data)


def corrected_variance(data):
    m = mean(data)
    total = 0
    for x in data:
        total += (x - m) ** 2
    return total / (len(data) - 1)


def std_deviation(variance):
    return variance ** 0.5


def statistical_series(data):
    frequency = {}
    for x in data:
        if x in frequency:
            frequency[x] += 1
        else:
            frequency[x] = 1
    relative_frequency = {}
    for x in frequency:
        relative_frequency[x] = frequency[x] / len(data)

    combined = {x: (frequency[x], relative_frequency[x]) for x in frequency}
    return combined


def interval_series(data):
    min_value, max_value = data[0], data[-1]
    n = len(data)
    num_intervals = math.ceil(1 + math.log2(n))  # По формуле Стерджесса
    interval_width = (max_value - min_value) /  (1 + math.log2(n))

    intervals = []
    start = min_value - (interval_width / 2)
    for i in range(num_intervals):
        end = start + interval_width
        count = sum(1 for x in data if start <= x < end)
        intervals.append((start, end, count, count / n))  # Добавляем частоту и относительную частоту
        start = end
    # Включаем последнее значение в последний интервал
    intervals[-1] = (
        intervals[-1][0],
        intervals[-1][1],
        intervals[-1][2] + 1,
        (intervals[-1][2] + 1) / n,
    )

    return intervals


def calculate_intervals(data):
    n = len(data)
    intervals = []
    for i in range(n - 1):
        cdf_value = (i + 1) / n
        intervals.append((data[i], data[i + 1], cdf_value))
    return intervals


def print_intervals(intervals):
    print("Эмпирическая функция распределения:")
    print(f"0.00    для x <= {intervals[0][0]:.2f}")
    for start, end, value in intervals:
        print(f"{value:.2f}    для {start:.2f} < x <= {end:.2f}")
    print(f"1.00    для x > {intervals[-1][1]:.2f}")


def empirical_cdf(data):
    cdf = []
    for x in data:
        f_x = sum(1 for val in data if val <= x) / len(data)  # Доля элементов <= x
        cdf.append(f_x)
    cdf.append(1)
    data.append(1)
    plt.step(data, cdf, where="post", label="ЭФР")
    plt.xlabel("Значение")
    plt.ylabel("F(x)")
    plt.title("Эмпирическая функция распределения")
    plt.grid()
    plt.legend()
    plt.show()


def reduced_frequencies_of_grouped_sample(data):
    n = len(data)
    k = 5  # Количество интервалов (можно выбрать вручную или рассчитать)
    h = (max(data) - min(data)) / k  # Длина интервала

    # Создаем интервалы
    intervals = [min(data) + i * h for i in range(k + 1)]  # Границы интервалов
    frequencies = [0] * k  # Частоты

    # Подсчёт частот
    for value in data:
        for i in range(k):
            if intervals[i] <= value < intervals[i + 1]:
                frequencies[i] += 1
                break
    frequencies[-1] += data.count(intervals[-1])

    # Плотности частот
    densities = [f / (n * h) for f in frequencies]

    # Центры интервалов для полигона частот
    bin_centers = [(intervals[i] + intervals[i + 1]) / 2 for i in range(k)]

    # Построение графиков
    plt.figure(figsize=(10, 6))

    # Гистограмма
    for i in range(k):
        plt.bar(bin_centers[i], densities[i], width=h, alpha=0.7, color='skyblue', edgecolor='black', align='center')

    # Полигон частот
    plt.plot(bin_centers, densities, marker='o', color='red', label='Полигон частот')
    plt.xlabel("Значение")
    plt.ylabel("Плотность частоты")
    plt.title("Гистограмма и полигон плотностей частот")
    plt.grid()
    plt.legend()
    plt.show()
