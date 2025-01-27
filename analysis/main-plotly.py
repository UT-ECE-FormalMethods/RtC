import json
import plotly.graph_objects as go
from plotly.subplots import make_subplots

def read_data(json_file):
    """Reads JSON data from the specified file."""
    with open(json_file, "r") as file:
        return json.load(file)

def generate_data(data, ignore_max_connectivity, convert_to_minutes):
    """Generates average heuristic times from test results."""
    heuristic_names = []
    heuristic_avg_times = {}

    for testcase in data["results"]:
        testcase_id = int(testcase["testcase_id_to_show"])
        for heuristic in testcase["heuristics"]:
            name = heuristic["name"]
            avg_time = (heuristic["avg_time"]/1000)  # converting to seconds

            if convert_to_minutes:
                avg_time /= 60

            if name == "max_connectivity" and ignore_max_connectivity:
                continue

            if name not in heuristic_names:
                heuristic_names.append(name)
                heuristic_avg_times[name] = []

            heuristic_avg_times[name].append((testcase_id, avg_time))
    
    return heuristic_avg_times

def plot_data_plotly(heuristic_avg_times, input_size_str, row, col, fig):
    """Adds heuristic performance data to the subplot."""
    for heuristic in heuristic_avg_times:
        heuristic_avg_times[heuristic].sort(key=lambda x: x[0])
        x = [item[0] for item in heuristic_avg_times[heuristic]]
        y = [item[1] for item in heuristic_avg_times[heuristic]]
        fig.add_trace(go.Scatter(x=x, y=y, mode='lines+markers', name=heuristic), row=row, col=col)

    fig.update_xaxes(title_text="Testcase ID", row=row, col=col)
    fig.update_yaxes(title_text="Average Time (s)", row=row, col=col)
    fig.update_layout(title=f"Performance of Heuristics - {input_size_str} input size")

# Load data
data_small = read_data('results-small.json')
data_medium = read_data('results-medium.json')
data_large = read_data('results-large.json')

# Generate data for both small and medium input sizes
heuristic_data_small = generate_data(data_small, False, False)
heuristic_data_medium = generate_data(data_medium, False, False)
heuristic_data_large = generate_data(data_large, False, True)

# Create a 2x2 subplot layout
fig = make_subplots(rows=2, cols=2, subplot_titles=("Small", "Medium", "Large", ""))

# Plot data
plot_data_plotly(heuristic_data_small, "Small", 1, 1, fig)
plot_data_plotly(heuristic_data_medium, "Medium", 1, 2, fig)
plot_data_plotly(heuristic_data_large, "Large", 2, 1, fig)

# Update layout settings
fig.update_layout(height=600, width=800, title_text="Heuristic Performance Comparison", showlegend=True)

# Show the figure
fig.show()
