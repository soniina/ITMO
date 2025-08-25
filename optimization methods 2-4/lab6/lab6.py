import random

distance_matrix = [
    [0, 1, 7, 2, 8],
    [2, 0, 10, 3, 1],
    [7, 10, 0, 2, 6],
    [2, 3, 2, 0, 4],
    [8, 1, 6, 4, 0]
]

CITIES = 5
N = 4
GENERATIONS = 50
MUTATION_RATE = 0.01


def route_length(route):
    return sum(distance_matrix[route[i]][route[(i + 1) % CITIES]] for i in range(CITIES))


def init_population():
    return [random.sample(range(CITIES), CITIES) for _ in range(N)]


def crossover(parent1, parent2):
    start, end = sorted(random.sample(range(CITIES), 2))

    child1 = [None] * len(parent1)
    child2 = [None] * len(parent2)

    child1[start:end+1] = parent2[start:end+1]
    child2[start:end+1] = parent1[start:end+1]

    parent_ptr = (start + 1) % CITIES
    for i in range(CITIES):
        if child1[i] is None:
            while parent1[parent_ptr] in child1:
                parent_ptr = (parent_ptr + 1) % CITIES
            child1[i] = parent1[parent_ptr]
            parent_ptr = (parent_ptr + 1) % CITIES

    parent_ptr = (start + 1) % CITIES
    for i in range(CITIES):
        if child2[i] is None:
            while parent2[parent_ptr] in child2:
                parent_ptr = (parent_ptr + 1) % CITIES
            child2[i] = parent2[parent_ptr]
            parent_ptr = (parent_ptr + 1) % CITIES

    return child1, child2


def mutate(route):
    if random.random() < MUTATION_RATE:
        i, j = random.sample(range(CITIES), 2)
        route[i], route[j] = route[j], route[i]


def select_best(population):
    return sorted(population, key=route_length)[:N]


def genetic_algorithm():
    population = init_population()
    best = min(population, key=route_length)

    for gen in range(GENERATIONS):
        parents = random.sample(population, 4)
        p1, p2, p3, p4 = parents

        c1, c2 = crossover(p1, p2)
        c3, c4 = crossover(p3, p4)

        for child in [c1, c2, c3, c4]:
            mutate(child)

        combined = [p1, p2, p3, p4, c1, c2, c3, c4]
        population = select_best(combined)

        current_best = min(population, key=route_length)
        if route_length(current_best) < route_length(best):
            best = current_best

        print(f"Поколение {gen + 1}: Кратчайший путь = {[c+1 for c in best]}, Длина пути = {route_length(best)}")

    return best


best_route = genetic_algorithm()
print("Кратчайший путь:", [city + 1 for city in best_route])
print("Длина пути:", route_length(best_route))
