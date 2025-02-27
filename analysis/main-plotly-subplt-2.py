import json
import plotly.graph_objects as go
from plotly.subplots import make_subplots

# Define colors for each heuristic
heuristic_colors = {
    "Incremental (No Heuristic)": "blue",
    "Min Transitions": "red",
    "Min States": "cyan",
    "Transition Density": "green",
    "Transition Disparity": "purple",
    "State Disparity": "orange",
    "Transition and State Product": "brown",
    "Max Connectivity": "gray"
}

# Define custom legend order (same for both plots)
custom_legend_order = [
    "Incremental (No Heuristic)",
    "Min Transitions",
    "Min States",
    "Transition Density",
    "Transition Disparity",
    "State Disparity", 
    "Transition and State Product", 
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
            avg_time = heuristic["avg_time"] / 1000  # convert from ms to s
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
    # Determine time unit based on dataset
    time_unit = "s" if input_size_str != "Large" else "min"
    
    # Create subplots: 1 row, 2 columns
    fig = make_subplots(
        rows=1, cols=2,
        subplot_titles=("Total Execution Time", "Intermediate CAs Size")
    )
    
    # For jitter in both plots
    jitter_amount = 0.75  
    
    num_heuristics = len(custom_legend_order)
    
    for heuristic_idx, heuristic in enumerate(custom_legend_order):
        # --- Left Subplot: Time Data ---
        if heuristic in time_data:
            
            if input_size_str == "Large":
              jitter_amount = 0.12
            elif input_size_str == "Medium":
                jitter_amount = 0.06
            elif input_size_str == "Small": 
                jitter_amount = 0.0035
            
            
            sorted_time = sorted(time_data[heuristic], key=lambda x: x[0])
            # Format x-values as: First letter of dataset + "-" + testcase_id
            x_time = [f"{input_size_str[0]}-{item[0]}" for item in sorted_time]
            # Add jitter only (always positive) to the y-values to avoid overlap
            y_time = [item[1] + (heuristic_idx+1) * jitter_amount for item in sorted_time]
            
            fig.add_trace(go.Scatter(
                x=x_time,
                y=y_time,
                mode='lines+markers',
                name=heuristic,
                marker_color=heuristic_colors.get(heuristic, "black"),
                line=dict(dash="dash") if heuristic == "Incremental (No Heuristic)" else {},
                showlegend=True  # Legend shown only here
            ), row=1, col=1)
        
        # --- Right Subplot: Size Data ---
        if heuristic in size_data:
            
            if input_size_str == "Large":
              jitter_amount = 7
            elif input_size_str == "Medium":
                jitter_amount = 0.75
            elif input_size_str == "Small": 
                jitter_amount = 0.75
            
            sorted_size = sorted(size_data[heuristic], key=lambda x: x[0])
            x_size = [f"{input_size_str[0]}-{item[0]}" for item in sorted_size]
            # Apply jitter (can be positive or negative) to the y-values to avoid overlap
            y_size = [item[1] + (heuristic_idx - (num_heuristics - 1) / 2) * jitter_amount for item in sorted_size]
            
            fig.add_trace(go.Scatter(
                x=x_size,
                y=y_size,
                mode='lines+markers',
                name=heuristic,
                marker_color=heuristic_colors.get(heuristic, "black"),
                line=dict(dash="dash") if heuristic == "Incremental (No Heuristic)" else {},
                showlegend=False  # Legend not shown on the right subplot
            ), row=1, col=2)
    
    # Update axes for the left subplot (Time)
    fig.update_xaxes(title_text="Testcase ID", type='category', row=1, col=1)
    fig.update_yaxes(title_text=f"Average Time ({time_unit})", row=1, col=1)
    
    # Update axes for the right subplot (Size)
    fig.update_xaxes(title_text="Testcase ID", type='category', row=1, col=2)
    fig.update_yaxes(title_text="Intermediate CAs Average Number of Transitions", row=1, col=2)
    
    # Set overall figure dimensions: 2000px width (2x1000) and 800px height
    fig.update_layout(
        title_text=f"Heuristic Performance - {input_size_str} Dataset",
        height=700,
        width=1600,
    )
    
    fig.show()

# ---------------- Main Execution ----------------

# File paths for time data
time_files = {
    "Small": "data/time/results-small.json",
    "Medium": "data/time/results-medium.json",
    "Large": "data/time/results-large.json"
}

# File paths for size data
size_files = {
    "Small": "data/size/results-small.json",
    "Medium": "data/size/results-medium.json",
    "Large": "data/size/results-large.json"
}

# Process and plot data for each dataset
for dataset in ["Small", "Medium", "Large"]:
    # Read JSON files for the current dataset
    time_json = read_data(time_files[dataset])
    size_json = read_data(size_files[dataset])
    
    # For the time data: convert to minutes for the "Large" dataset, seconds otherwise.
    convert_to_minutes = True if dataset == "Large" else False
    time_data = generate_time_data(time_json, ignore_max_connectivity=False, convert_to_minutes=convert_to_minutes)
    
    size_data = generate_size_data(size_json, ignore_max_connectivity=False)
    
    # Generate the combined plot (1 row, 2 columns) for the current dataset
    plot_combined_data(time_data, size_data, dataset)
