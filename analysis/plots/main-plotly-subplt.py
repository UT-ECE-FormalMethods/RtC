import json
import plotly.graph_objects as go
from plotly.subplots import make_subplots

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

custom_legend_order = [
    "Incremental (No Heuristic)",
    "Min Transitions",
    "Min States",
    "Transition Density",
    "Transition Disparity",
    "State Disparity", 
    "Transition and State Sum", 
    "Max Connectivity"
]

def read_data(json_file):
    with open(json_file, "r") as file:
        return json.load(file)

def generate_time_data(data, ignore_max_connectivity, convert_to_minutes):
    """
    Processes the time JSON data.
    Returns a dictionary with keys as heuristic names and values as lists of
    tuples (testcase_id, avg_time). For the "Large" dataset, avg_time is converted to minutes.
    """
    heuristic_avg_times = {}
    for testcase in data["results"]:
        testcase_id = int(testcase["testcase_id_to_show"])
        for heuristic in testcase["heuristics"]:
            name = heuristic["name"]
            avg_time = heuristic["avg_time"] / 1000 
            if convert_to_minutes:
                avg_time /= 60
            if name == "max_connectivity" and ignore_max_connectivity:
                continue
            if name not in heuristic_avg_times:
                heuristic_avg_times[name] = []
            heuristic_avg_times[name].append((testcase_id, avg_time))
    return heuristic_avg_times

def generate_size_data(data, ignore_max_connectivity):
    """
    Processes the size JSON data.
    Returns a dictionary with keys as heuristic names and values as lists of
    tuples (testcase_id, avg_intermediate_CA_transitions).
    """
    heuristic_avg_intermediate_CA_transitions = {}
    for testcase in data["results"]:
        testcase_id = int(testcase["testcase_id_to_show"])
        for heuristic in testcase["heuristics"]:
            name = heuristic["name"]
            avg_intermediate_CA_transitions = heuristic["avg_intermediate_CA_transitions"]
            if name not in heuristic_avg_intermediate_CA_transitions:
                heuristic_avg_intermediate_CA_transitions[name] = []
            heuristic_avg_intermediate_CA_transitions[name].append((testcase_id, avg_intermediate_CA_transitions))
    return heuristic_avg_intermediate_CA_transitions

def plot_combined_data(time_data, size_data, input_size_str):
    """
    Creates a combined plot with 1 row and 2 columns:
      - Left subplot: Total Execution Time.
      - Right subplot: Intermediate CAs Average Number of Transitions.
    Legends are shown only in the left subplot.
    Each subplot is 1000x800, resulting in an overall figure of 2000x800.
    """

    time_unit = "s" if input_size_str != "Large" else "min"
    
    fig = make_subplots(
        rows=1, cols=2,
        subplot_titles=("Total Execution Time", "Intermediate CAs Size")
    )
    
    jitter_amount = 0
    if input_size_str == "Large":
      jitter_amount = 0
    elif input_size_str == "Medium":
        jitter_amount = 0.06
    elif input_size_str == "Small": 
        jitter_amount = 0
      
    num_heuristics = len(custom_legend_order)
    
    for heuristic_idx, heuristic in enumerate(custom_legend_order):
        if heuristic in time_data:
            sorted_time = sorted(time_data[heuristic], key=lambda x: x[0])
            
            x_time = [f"{input_size_str[0]}-{item[0]}" for item in sorted_time]
            
            y_time = [item[1] + (heuristic_idx - (num_heuristics - 1) / 2) * jitter_amount for item in sorted_time]
            
            fig.add_trace(go.Scatter(
                x=x_time,
                y=y_time,
                mode='lines+markers',
                name=heuristic,
                marker_color=heuristic_colors.get(heuristic, "black"),
                line=dict(dash="dash") if heuristic == "Incremental (No Heuristic)" else {},
                showlegend=True  
            ), row=1, col=1)
        
        if heuristic in size_data:
            sorted_size = sorted(size_data[heuristic], key=lambda x: x[0])
            x_size = [f"{input_size_str[0]}-{item[0]}" for item in sorted_size]

            y_size = [item[1] + (heuristic_idx - (num_heuristics - 1) / 2) * jitter_amount for item in sorted_size]
            
            fig.add_trace(go.Scatter(
                x=x_size,
                y=y_size,
                mode='lines+markers',
                name=heuristic,
                marker_color=heuristic_colors.get(heuristic, "black"),
                line=dict(dash="dash") if heuristic == "Incremental (No Heuristic)" else {},
                showlegend=False  
            ), row=1, col=2)

    fig.update_xaxes(title_text="Testcase ID", type='category', row=1, col=1)
    fig.update_yaxes(title_text=f"Average Time ({time_unit})", row=1, col=1)
    
    fig.update_xaxes(title_text="Testcase ID", type='category', row=1, col=2)
    fig.update_yaxes(title_text="Intermediate CAs Average Number of Transitions", row=1, col=2)
    
    fig.update_layout(
        title_text=f"Heuristic Performance - {input_size_str} Dataset",
        height=700,
        width=1600,
    )
    
    fig.show()


time_files = {
    "Small": "data/time/results-small.json",
    "Medium": "data/time/results-medium.json",
    "Large": "data/time/results-large.json"
}

size_files = {
    "Small": "data/size/results-small.json",
    "Medium": "data/size/results-medium.json",
    "Large": "data/size/results-large.json"
}

for dataset in ["Small", "Medium", "Large"]:
    time_json = read_data(time_files[dataset])
    size_json = read_data(size_files[dataset])
    
    convert_to_minutes = True if dataset == "Large" else False
    time_data = generate_time_data(time_json, ignore_max_connectivity=False, convert_to_minutes=convert_to_minutes)
    
    size_data = generate_size_data(size_json, ignore_max_connectivity=False)
    
    plot_combined_data(time_data, size_data, dataset)
