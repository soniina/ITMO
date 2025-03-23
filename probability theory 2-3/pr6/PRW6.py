from probabilityTheory import *

nums = [-0.53, -0.93, 0.48, -1.55, -1.34, -0.04, -0.84, 0.57, 0.76, 0.30, -0.87, -0.41, 0.81, -1.42, -0.61,
        -0.33, -1.33, 0.62, -0.48, -0.35]

nums = variational_series(nums)
for i in range(0, len(nums), 10):
    print(' '.join(f"{num}" for num in nums[i:i+10]))

print(f"Вариационный ряд: {', '.join(map(str, nums))}")
print(f"Экстремальные значения: MIN = {extreme_values(nums)[0]}, MAX = {extreme_values(nums)[1]}")
print(f"Размах: {span(nums)}")
print(f"Медиана: {median(nums)}")
# print(f"Мода: {', '.join(map(str, mode(nums)))}")

mean_value = mean(nums)
sample_var = sample_variance(nums)
corrected_var = corrected_variance(nums)
sample_std = std_deviation(sample_var)
corrected_std = std_deviation(corrected_var)
stat_series = statistical_series(nums)
interval_series_data = interval_series(nums)

print(f"Выборочное среднее: {mean_value}")
print(f"Выборочная дисперсия: {sample_var}")
print(f"Исправленная дисперсия: {corrected_var}")
print(f"Выборочное среднее квадратичное отклонение: {sample_std}")
print(f"Исправленное среднее квадратичное отклонение: {corrected_std}")

print("\nСтатистический ряд:")
print("Значение  | Частота | Частнотность")
for value, (freq, rel_freq) in sorted(stat_series.items()):
    print(f"{value:9.2f} | {freq:7} | {rel_freq:.2f}")

print("\nИнтервальный ряд:")
print("Интервал       | Частота     | Относительная частота")
for start, end, count, rel_freq in interval_series_data:
    print(f"[{start:5.2f}, {end:5.2f}) | {count:11} | {rel_freq:.2f}")
print()

intervals = calculate_intervals(nums)
print_intervals(intervals)

empirical_cdf(nums)
reduced_frequencies_of_grouped_sample(nums)

