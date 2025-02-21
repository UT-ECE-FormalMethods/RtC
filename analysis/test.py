import json
import plotly.graph_objects as go

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
    "Incremental (No Heuristic)": "blue",
    "Min Transitions": "red",
    "Min States": "cyan",
    "Transition Density": "green",
    "Transition Disparity": "purple",
    "State Disparity": "orange",
    "Transition and State Sum": "brown",
    "Max Connectivity": "gray"
}

def plot_data_plotly(heuristic_avg_times, input_size_str, zoom_in=False):
    time_unit = "s" if input_size_str != "Large" else "min"
    fig = go.Figure()
    
    for heuristic in heuristic_avg_times:
        heuristic_avg_times[heuristic].sort(key=lambda x: x[0])
        x = [input_size_str[0] + "-" + str(item[0]) for item in heuristic_avg_times[heuristic]]
        y = [item[1] for item in heuristic_avg_times[heuristic]]
        fig.add_trace(go.Scatter(
            x=x, y=y, mode='lines+markers',
            name=heuristic,
            marker_color=heuristic_colors.get(heuristic, "black")
        ))
    
    fig.update_xaxes(title_text="Testcase ID", type='category')
    fig.update_yaxes(title_text=f"Average Time ({time_unit})")
    fig.update_layout(title_text=f"Heuristic Performance - {input_size_str}", height=800, width=1000)
    
    if zoom_in:
        # Create an inset figure
        inset_fig = go.Figure()
        
        # Define the zoom region
        zoom_region = {'x': ['L-2', 'L-4'], 'y': [0, 5]}
        
        for heuristic in heuristic_avg_times:
            x = [input_size_str[0] + "-" + str(item[0]) for item in heuristic_avg_times[heuristic]]
            y = [item[1] for item in heuristic_avg_times[heuristic]]
            
            # Filter data within the zoom region
            filtered_x = [xi for xi, yi in zip(x, y) if zoom_region['x'][0] <= xi <= zoom_region['x'][1]]
            filtered_y = [yi for xi, yi in zip(x, y) if zoom_region['x'][0] <= xi <= zoom_region['x'][1]]
            
            inset_fig.add_trace(go.Scatter(
                x=filtered_x, y=filtered_y, mode='lines+markers',
                name=heuristic,
                marker_color=heuristic_colors.get(heuristic, "black"),
                showlegend=False
            ))
        
        inset_fig.update_xaxes(range=[zoom_region['x'][0], zoom_region['x'][1]], title_text="")
        inset_fig.update_yaxes(range=[zoom_region['y'][0], zoom_region['y'][1]], title_text="")
        inset_fig.update_layout(height=300, width=400, margin=dict(l=0, r=0, t=0, b=0))
        
        # Add the inset figure as an annotation
        fig.add_trace(inset_fig.data[0])
        fig.add_annotation(
            xref="paper", yref="paper",
            x=0.7, y=0.7,
            text="Zoomed In",
            showarrow=False,
            font=dict(size=12)
        )
        fig.add_vrect(
            x0=zoom_region['x'][0], x1=zoom_region['x'][1],
            fillcolor="LightSalmon", opacity=0.3,
            layer="below", line_width=0,
        )
        fig.add_hrect(
            y0=zoom_region['y'][0], y1=zoom_region['y'][1],
            fillcolor="LightSalmon", opacity=0.3,
            layer="below", line_width=0,
        )
    
    fig.show()

# Load Data
data_small  = read_data('data/time/results-small.json')
data_medium = read_data('data/time/results-medium.json')
data_large  = read_data('data/time/results-large.json')

# Generate Data
heuristic_data_small  = generate_data(data_small, False, False)
heuristic_data_medium = generate_data(data_medium, False, False)
heuristic_data_large  = generate_data(data_large, False, True)

# Generate separate plots
plot_data_plotly(heuristic_data_small, "Small", zoom_in=True)
plot_data_plotly(heuristic_data_medium, "Medium", zoom_in=True)
plot_data_plotly(heuristic_data_large, "Large", zoom_in=True)