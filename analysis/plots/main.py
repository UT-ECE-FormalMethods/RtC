import json
import matplotlib.pyplot as plt

def read_data(json_file):
    with open(json_file, "r") as file:
        return json.load(file)

def generate_data(data, ignore_max_connectivity, convert_to_minutes):
    heuristic_names = []
    heuristic_avg_times = {}

    for testcase in data["results"]:
        testcase_id = int(testcase["testcase_id_to_show"])
        for heuristic in testcase["heuristics"]:
            name = heuristic["name"]
            avg_time = (heuristic["avg_time"]/1000) 

            if convert_to_minutes:
                avg_time /= 60

            if name == "max_connectivity" and ignore_max_connectivity:
                continue

            if name not in heuristic_names:
                heuristic_names.append(name)
                heuristic_avg_times[name] = []

            heuristic_avg_times[name].append((testcase_id, avg_time))
    
    return heuristic_avg_times


def plot_data(heuristic_avg_times, fig_id, ax, input_size_str):
    for heuristic in heuristic_avg_times:
        heuristic_avg_times[heuristic].sort(key=lambda x: x[0])

    for heuristic in heuristic_avg_times:
        x = [item[0] for item in heuristic_avg_times[heuristic]]
        y = [item[1] for item in heuristic_avg_times[heuristic]]
        ax.plot(x, y, marker='o', label=heuristic)

    ax.set_xlabel("Testcase ID", fontsize=12)
    ax.set_ylabel("Average Time (s)", fontsize=12)
    ax.set_title(f"Performance of Heuristics - {input_size_str} input size - figure {fig_id}", fontsize=14)
    ax.legend(title="Heuristics", fontsize=10)
    ax.grid(alpha=0.4)

    all_testcase_ids = sorted(set([item[0] for heuristic in heuristic_avg_times.values() for item in heuristic]))
    ax.set_xticks(all_testcase_ids)
    ax.set_xticklabels([str(tc) for tc in all_testcase_ids])
    

data_small = read_data('results-small.json')
data_medium = read_data('results-medium.json')
data_large = read_data('results-large.json')

heuristic_data_small = generate_data(data_small, False, False)
heuristic_data_medium = generate_data(data_medium, False, False)
heuristic_data_large = generate_data(data_large, False, True)

fig, axes = plt.subplots(2, 2, figsize=(16, 12))  # 1 row, 2 columns

plot_data(heuristic_data_small, 1, axes[0, 0], "Small")

plot_data(heuristic_data_medium, 2, axes[0, 1], "Medium")

plot_data(heuristic_data_large, 3, axes[1, 0], "Large")

axes[1, 1].axis('off') 
axes[0, 0].legend(title="Heuristics", fontsize=10, loc='best')  


plt.tight_layout()
plt.show()
