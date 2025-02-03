import json
import plotly.graph_objects as go
from plotly.subplots import make_subplots

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
            avg_time = heuristic["avg_time"] / 1000  # convert to seconds
            if convert_to_minutes:
                avg_time /= 60
            if name == "max_connectivity" and ignore_max_connectivity:
                continue
            if name not in heuristic_names:
                heuristic_names.append(name)
                heuristic_avg_times[name] = []
            heuristic_avg_times[name].append((testcase_id, avg_time))
    return heuristic_avg_times

# Define colors for each heuristic
heuristic_colors = {
    "no_heuristic": "blue",
    "min_transitions": "red",
    "min_states": "cyan",
    "transition_density": "green",
    "transition_disparity": "purple",
    "state_disparity": "orange",
    "transition_and_state_sum": "brown",
    "max_connectivity": "gray"
}

def plot_data_plotly(heuristic_avg_times, input_size_str, row, col, fig, show_legend):
    time_unit = "s"  # Default time unit
    if input_size_str == "Large":
        time_unit = "min"  # Change to minutes for the "Large" subplot
    for heuristic in heuristic_avg_times:
        heuristic_avg_times[heuristic].sort(key=lambda x: x[0])
        x = [item[0] for item in heuristic_avg_times[heuristic]]
        y = [item[1] for item in heuristic_avg_times[heuristic]]
        fig.add_trace(go.Scatter(
            x=x, y=y, mode='lines+markers',
            name=heuristic,
            marker_color=heuristic_colors[heuristic],
            showlegend=show_legend  # Control legend visibility based on input parameter
        ), row=row, col=col)
    fig.update_xaxes(title_text="Testcase ID", row=row, col=col, type='category')
    fig.update_yaxes(title_text=f"Average Time ({time_unit})", row=row, col=col)

# Load Data
data_small = read_data('results-small.json')
data_medium = read_data('results-medium.json')
data_large = read_data('results-large.json')

# Generate Data
heuristic_data_small = generate_data(data_small, False, False)
heuristic_data_medium = generate_data(data_medium, False, False)
heuristic_data_large = generate_data(data_large, False, True)

fig = make_subplots(rows=2, cols=2, subplot_titles=("Small", "Medium", "Large", ""))

# Call plot_data_plotly for each dataset, controlling legend visibility
plot_data_plotly(heuristic_data_small, "Small", 1, 1, fig, show_legend=False)
plot_data_plotly(heuristic_data_medium, "Medium", 1, 2, fig, show_legend=False)
plot_data_plotly(heuristic_data_large, "Large", 2, 1, fig, show_legend=True)

# Update layout
fig.update_layout(height=1500, width=1500, title_text="Heuristic Performance Comparison")

# Show the figure
fig.show()
