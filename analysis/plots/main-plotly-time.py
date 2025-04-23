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
            avg_time = heuristic["avg_time"] / 1000  
            if convert_to_minutes:
                avg_time /= 60
            if name == "max_connectivity" and ignore_max_connectivity:
                continue
            if name not in heuristic_names:
                heuristic_names.append(name)
                heuristic_avg_times[name] = []
            heuristic_avg_times[name].append((testcase_id, avg_time))
    return heuristic_avg_times

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


def plot_data_plotly(heuristic_avg_times, input_size_str):
    time_unit = "s" if input_size_str != "Large" else "min"
    fig = go.Figure()

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

    for heuristic in custom_legend_order:  
        if heuristic in heuristic_avg_times:  
            heuristic_avg_times[heuristic].sort(key=lambda x: x[0])
            x = [input_size_str[0] + "-" + str(item[0]) for item in heuristic_avg_times[heuristic]]
            y = [item[1] for item in heuristic_avg_times[heuristic]]
            fig.add_trace(go.Scatter(
                x=x, y=y, mode='lines+markers',
                name=heuristic,
                marker_color=heuristic_colors[heuristic],
                line=dict(dash="dash") if heuristic == "Incremental (No Heuristic)" else None  
            ))

    fig.update_xaxes(title_text="Testcase ID", type='category')
    fig.update_yaxes(title_text=f"Average Time ({time_unit})")
    fig.update_layout(
        title_text=f"Heuristic Performance - Total Execution Time - {input_size_str}",
        height=800, width=1000,
        legend=dict(traceorder="normal")  
    )

    fig.show()

data_small  = read_data('data/time/results-small.json')
data_medium = read_data('data/time/results-medium.json')
data_large  = read_data('data/time/results-large.json')

heuristic_data_small  = generate_data(data_small, False, False)
heuristic_data_medium = generate_data(data_medium, False, False)
heuristic_data_large  = generate_data(data_large, False, True)

plot_data_plotly(heuristic_data_small, "Small")
plot_data_plotly(heuristic_data_medium, "Medium")
plot_data_plotly(heuristic_data_large, "Large")
