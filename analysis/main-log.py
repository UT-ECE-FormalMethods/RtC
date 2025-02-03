import json
import matplotlib.pyplot as plt
import numpy as np

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
            avg_time = (heuristic["avg_time"]/1000)  # converting to seconds

            if name == "max_connectivity" and ignore_max_connectivity:
                continue

            if name not in heuristic_names:
                heuristic_names.append(name)
                heuristic_avg_times[name] = []

            heuristic_avg_times[name].append((testcase_id, avg_time))
    
    return heuristic_avg_times

def plot_data(heuristic_avg_times_small, heuristic_avg_times_medium, fig_id):
    plt.figure(figsize=(12, 6))

    # Fixed color map for 8 distinct heuristics
    color_map = plt.get_cmap("tab10")  # Get a colormap with at least 10 distinct colors

    # Prepare a dictionary to store the assigned colors for each heuristic
    heuristic_colors = {}
    heuristic_index = 0

    markers = ['o', 's', '^', 'v', '<', '>', 'D', 'P']  # List of marker styles

    # Plot small dataset
    for heuristic in heuristic_avg_times_small:
        if heuristic not in heuristic_colors:  # Assign a color if not already assigned
            heuristic_colors[heuristic] = color_map(heuristic_index % 8)  # Use 8 colors max
            heuristic_index += 1
        
        heuristic_avg_times_small[heuristic].sort(key=lambda x: x[0])
        x_small = [item[0] for item in heuristic_avg_times_small[heuristic]]
        y_small = [item[1] for item in heuristic_avg_times_small[heuristic]]
        plt.plot(x_small, y_small, marker=markers[list(heuristic_colors.keys()).index(heuristic)], linestyle='-', color=heuristic_colors[heuristic], label=f'{heuristic} (small)')

    # Plot medium dataset
    for heuristic in heuristic_avg_times_medium:
        if heuristic not in heuristic_colors:  # Assign a color if not already assigned
            heuristic_colors[heuristic] = color_map(heuristic_index % 8)  # Use 8 colors max
            heuristic_index += 1

        heuristic_avg_times_medium[heuristic].sort(key=lambda x: x[0])
        x_medium = [item[0] for item in heuristic_avg_times_medium[heuristic]]
        y_medium = [item[1] for item in heuristic_avg_times_medium[heuristic]]
        plt.plot(x_medium, y_medium, marker=markers[list(heuristic_colors.keys()).index(heuristic)], linestyle='-', color=heuristic_colors[heuristic], label=f'{heuristic} (medium)')

    plt.xlabel("Testcase ID", fontsize=12)
    plt.ylabel("Average Time (s) - Log Scale", fontsize=12)
    plt.title(f"Performance of Heuristics - Combined Plot - figure {fig_id}", fontsize=14)
    plt.legend(title="Heuristics", fontsize=10)
    plt.grid(alpha=0.4)
    plt.yscale('log')

    all_testcase_ids = sorted(set([item[0] for heuristic in heuristic_avg_times_small.values() for item in heuristic] +
                                  [item[0] for heuristic in heuristic_avg_times_medium.values() for item in heuristic]))
    plt.xticks(all_testcase_ids, [str(tc) for tc in all_testcase_ids])
    
    plt.tight_layout()
    plt.show()

# Load data
data_small = read_data('results-small.json')
data_medium = read_data('results-medium.json')

# Generate data for both small and medium input sizes
heuristic_data_small = generate_data(data_small, False)
heuristic_data_medium = generate_data(data_medium, False)

# Plot both datasets together with log y-scale
plot_data(heuristic_data_small, heuristic_data_medium, 1)
