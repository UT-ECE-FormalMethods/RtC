import json
import plotly.graph_objects as go
from plotly.subplots import make_subplots

# Define colors for each heuristic
heuristic_colors = {
    "Default (No Heuristic)": "blue",
    "Min Transitions": "red",
    "Min States": "deepskyblue",
    "Transition Density": "green",
    "Transition Disparity": "purple",
    "State Disparity": "orange",
    "Transition and State Product": "hotpink",
    "Max Connectivity": "gray",
    "Transition and Connectivity Product": "yellowgreen",
    "State and Connectivity Product": "teal"
}

# Define custom legend order (same for both plots)
custom_legend_order = [
    "Default (No Heuristic)",
    "Min Transitions",
    "Min States",
    "Transition Density",
    "Transition Disparity",
    "State Disparity",  
    "Max Connectivity",
    "Transition and State Product",
    "Transition and Connectivity Product",
    "State and Connectivity Product"
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
    if input_size_str == "Small" or input_size_str == "Medium" or input_size_str == "Medium-Combined":
        time_unit = "s"
    if input_size_str == "Large" or input_size_str == "X-Large" or input_size_str == "XL-Combined":
        time_unit = "min"
    
    # Create subplots: 1 row, 2 columns
    fig = make_subplots(
        rows=1, cols=2,
        subplot_titles=("Total Execution Time", "Intermediate CAs Size")
    )
    
    # For jitter in both plots
    jitter_amount = 0  
    
    num_heuristics = len(custom_legend_order)
    
    heuristic_prefix = input_size_str[0]
    if input_size_str == "X-Large" or input_size_str == "XL-Combined":
        heuristic_prefix = "XL"
    
    for heuristic_idx, heuristic in enumerate(custom_legend_order):
        # --- Left Subplot: Time Data ---
        if heuristic in time_data:
            
            if input_size_str == "X-Large":
                jitter_amount = 0.5
            elif input_size_str == "Large":
              jitter_amount = 0.12
            elif input_size_str == "Medium":
                jitter_amount = 0.06
            elif input_size_str == "Small": 
                jitter_amount = 0.0035
            elif input_size_str == "XL-Combined":
                jitter_amount = 0.2
            elif input_size_str == "Medium-Combined":
                jitter_amount = 0.02
            
            sorted_time = sorted(time_data[heuristic], key=lambda x: x[0])
            # Format x-values as: First letter of dataset + "-" + testcase_id
            x_time = [f"{heuristic_prefix}-{item[0]}" for item in sorted_time]
            # Add jitter only (always positive) to the y-values to avoid overlap
            y_time = [item[1] + (heuristic_idx+1) * jitter_amount for item in sorted_time]
            
            fig.add_trace(go.Scatter(
                x=x_time,
                y=y_time,
                mode='lines+markers',
                name=heuristic,
                marker_color=heuristic_colors.get(heuristic, "black"),
                line=dict(dash="dash") if heuristic == "Default (No Heuristic)" else {},
                showlegend=True  # Legend shown only here
            ), row=1, col=1)
        
        # --- Right Subplot: Size Data ---
        if heuristic in size_data:
            
            if input_size_str == "X-Large":
                jitter_amount = 3
            elif input_size_str == "Large":
              jitter_amount = 7
            elif input_size_str == "Medium":
                jitter_amount = 3
            elif input_size_str == "Small": 
                jitter_amount = 0.75
            elif input_size_str == "XL-Combined":
                jitter_amount = 3
            elif input_size_str == "Medium-Combined":
                jitter_amount = 1
            
            sorted_size = sorted(size_data[heuristic], key=lambda x: x[0])
            x_size = [f"{heuristic_prefix}-{item[0]}" for item in sorted_size]
            # Apply jitter (can be positive or negative) to the y-values to avoid overlap
            y_size = [item[1] + (heuristic_idx+1) * jitter_amount for item in sorted_size]
            
            fig.add_trace(go.Scatter(
                x=x_size,
                y=y_size,
                mode='lines+markers',
                name=heuristic,
                marker_color=heuristic_colors.get(heuristic, "black"),
                line=dict(dash="dash") if heuristic == "Default (No Heuristic)" else {},
                showlegend=False  # Legend not shown on the right subplot
            ), row=1, col=2)
    
    # Update axes for the left subplot (Time)
    fig.update_xaxes(title_text="Testcase ID", type='category', row=1, col=1)
    fig.update_yaxes(title_text=f"Average Time ({time_unit})", row=1, col=1)
    
    # Update axes for the right subplot (Size)
    fig.update_xaxes(title_text="Testcase ID", type='category', row=1, col=2)
    fig.update_yaxes(title_text="Intermediate CAs Average Number of Transitions", row=1, col=2)
    
    # Set overall figure dimensions: 2000px width (2x1000) and 800px height
    fig_width = 1600
    dataset_title_name = input_size_str
    fig_title = f"Heuristic Performance - {dataset_title_name} Dataset"
    
    if input_size_str == "XL-Combined" or input_size_str == "Medium-Combined":
        fig_width = 1685
        dataset_title_name = "X-Large" if input_size_str == "XL-Combined" else "Medium"
        fig_title = f"Combined Heuristics Performance - {dataset_title_name} Dataset"
        
    fig.update_layout(
        title_text=fig_title,
        height=700,
        width=fig_width,
    )
    
    fig.show()

# ---------------- Main Execution ----------------

# File paths for time data
time_files = {
    "Small": "data/time/results-small.json",
    "Medium": "data/time/results-medium.json",
    "Large": "data/time/results-large.json",
    "X-Large": "data/time/results-xl.json",
    "XL-Combined": "data/time/results-xl-combined.json",
    "Medium-Combined": "data/time/results-medium-combined.json"
}

# File paths for size data
size_files = {
    "Small": "data/size/results-small.json",
    "Medium": "data/size/results-medium.json",
    "Large": "data/size/results-large.json",
    "X-Large": "data/size/results-xl.json",
    "XL-Combined": "data/size/results-xl-combined.json",
    "Medium-Combined": "data/size/results-medium-combined.json"
}

# Process and plot data for each dataset
for dataset in ["Small", "Medium", "Large", "X-Large", "Medium-Combined", "XL-Combined"]:
    # Read JSON files for the current dataset
    time_json = read_data(time_files[dataset])
    size_json = read_data(size_files[dataset])
    
    # For the time data: convert to minutes for the "Large" dataset, seconds otherwise.
    convert_to_minutes = False
    if dataset == "Large" or dataset == "X-Large" or dataset == "XL-Combined":
        convert_to_minutes = True
    # convert_to_minutes = True if dataset == "Large" else False
    time_data = generate_time_data(time_json, ignore_max_connectivity=False, convert_to_minutes=convert_to_minutes)
    
    size_data = generate_size_data(size_json, ignore_max_connectivity=False)
    
    # Generate the combined plot (1 row, 2 columns) for the current dataset
    plot_combined_data(time_data, size_data, dataset)
