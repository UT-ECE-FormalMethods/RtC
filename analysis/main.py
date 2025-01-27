import json
import matplotlib.pyplot as plt


def read_data(json_file):
    with open(json_file, "r") as file:
        return json.load(file)

def generate_data(data, ignore_max_connectivity):
    heuristic_names = []
    heuristic_avg_times = {}

    for testcase in data["results"]:
        testcase_id = int(testcase["testcase_id_to_show"])
        for heuristic in testcase["heuristics"]:
            name = heuristic["name"]
            avg_time = (heuristic["avg_time"]/1000) #converting to seconds

            if name == "max_connectivity" and ignore_max_connectivity:
                continue

            if name not in heuristic_names:
                heuristic_names.append(name)
                heuristic_avg_times[name] = []

            heuristic_avg_times[name].append((testcase_id, avg_time))
    
    return heuristic_avg_times


def plot_data(heuristic_avg_times, fig_id):
    for heuristic in heuristic_avg_times:
        heuristic_avg_times[heuristic].sort(key=lambda x: x[0])

    plt.figure(figsize=(10, 6))
    for heuristic in heuristic_avg_times:
        x = [item[0] for item in heuristic_avg_times[heuristic]]
        y = [item[1] for item in heuristic_avg_times[heuristic]]
        plt.plot(x, y, marker='o', label=heuristic)

    plt.xlabel("Testcase ID", fontsize=12)
    plt.ylabel("Average Time (s)", fontsize=12)
    plt.title(f"Performance of Heuristics - Medium input size - figure {fig_id}", fontsize=14)
    plt.legend(title="Heuristics", fontsize=10)
    plt.grid(alpha=0.4)

    all_testcase_ids = sorted(set([item[0] for heuristic in heuristic_avg_times.values() for item in heuristic]))
    plt.xticks(all_testcase_ids, [str(tc) for tc in all_testcase_ids])

    plt.tight_layout()
    plt.show()


data = read_data('results-medium.json')
heuristic_data = generate_data(data, False)
plot_data(heuristic_data, 1)

# heuristic_data = generate_data(data, True)
# plot_data(heuristic_data, 2)