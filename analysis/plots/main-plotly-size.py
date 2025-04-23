import json
import plotly.graph_objects as go

def read_data(json_file):
    with open(json_file, "r") as file:
        return json.load(file)

def generate_data(data, ignore_max_connectivity):
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

def plot_data_plotly(avg_intermediate_CA_transitions, input_size_str):
    fig = go.Figure()
    
    jitter_amount = 0.75  
    num_heuristics = len(custom_legend_order)

    testcase_ids = sorted({item[0] for data in avg_intermediate_CA_transitions.values() for item in data})

    for heuristic_idx, heuristic in enumerate(custom_legend_order):
        if heuristic in avg_intermediate_CA_transitions:  
            data = avg_intermediate_CA_transitions[heuristic]
            data.sort(key=lambda x: x[0])

            x_values = [item[0] for item in data]
            y_values = [item[1] + (heuristic_idx - (num_heuristics - 1)/2) * jitter_amount for item in data]

            fig.add_trace(go.Scatter(
                x=[f"{input_size_str[0]}-{tid}" for tid in x_values],
                y=y_values,
                mode='lines+markers',
                name=heuristic,
                marker_color=heuristic_colors.get(heuristic, "black"),
                line=dict(dash="dash") if heuristic == "Incremental (No Heuristic)" else None
            ))
    
    fig.update_xaxes(title_text="Testcase ID", type='category')
    fig.update_yaxes(title_text=f"Intermediate CAs Average Number of Transitions")
    fig.update_layout(
        title_text=f"Heuristic Performance - Intermediate CAs Size - {input_size_str}",
        height=800,
        width=1000,
        legend=dict(traceorder="normal")  
    )
    fig.show()    


data_small = read_data('data/size/results-small.json')
data_medium = read_data('data/size/results-medium.json')
data_large = read_data('data/size/results-large.json')

heuristic_data_small = generate_data(data_small, False)
heuristic_data_medium = generate_data(data_medium, False)
heuristic_data_large = generate_data(data_large, False)

plot_data_plotly(heuristic_data_small, "Small")
plot_data_plotly(heuristic_data_medium, "Medium")
plot_data_plotly(heuristic_data_large, "Large")
