package org.example.exceptions;

public class WrongHeuristicTypeSelectionException extends Exception {
    public WrongHeuristicTypeSelectionException() {
        super("""
                Wrong heuristic selected!
                Options are as following:
                (0)  for  {No Heuristic (Default)}
                (1)  for  {Min Transitions}
                (2)  for  {Min States}
                (3)  for  {Transition Density}
                (4)  for  {Transition Disparity}
                (5)  for  {State Disparity}
                (6)  for  {Transition and State Harmonic Mean}
                (7)  for  {Transition and State Product}
                (8)  for  {Max Connectivity}
                (9)  for  {Min Transitions and Max Connectivity Combination}
                (10) for  {Min States and Max Connectivity Combination}
                """);
    }
}
