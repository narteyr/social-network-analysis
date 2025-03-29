## SOCIAL NETWORK ANALYSIS
# Bacon Number Finder

This project is inspired by the "Bacon Number" concept, which measures the separation between actors based on their shared film appearances. The goal is to determine the shortest path between any given actor and a target actor (traditionally Kevin Bacon) through their mutual co-stars.

## Background

In the Kevin Bacon game, actors are represented as vertices in a graph, and an edge exists between two actors if they have appeared together in a movie. The objective is to find the shortest path between two actors, indicating their degree of separation. This project allows flexibility in choosing any actor as the center of the acting universe, not just Kevin Bacon.

## Features

- **Shortest Path Calculation**: Determine the minimal number of connections between two actors.
- **Customizable Center**: Set any actor as the central figure to measure separations from.
- **Actor Statistics**: Analyze actors based on their connectivity and separation metrics within the network.

## Commands

The application supports the following commands:

- `c <#>`: List the top (positive number) or bottom (negative number) `<#>` centers of the universe, sorted by average separation.
- `d <low> <high>`: List actors sorted by degree, with degrees between `<low>` and `<high>`.
- `i`: List actors with infinite separation from the current center.
- `p <name>`: Find the path from `<name>` to the current center of the universe.
- `s <low> <high>`: List actors sorted by non-infinite separation from the current center, with separation between `<low>` and `<high>`.
- `u <name>`: Set `<name>` as the new center of the universe.
- `q`: Quit the application.

## Sample Interactions