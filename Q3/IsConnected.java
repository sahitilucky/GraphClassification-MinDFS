public class IsConnected {

	public int[] visited;

	public boolean isConnected(Graph g) {
		visited = new int[g.noofnodes];
		for (int i = 0; i < g.noofnodes; i++)
			visited[i] = 0;

		visited[0] = 1;
		int no_of_comp = 1, j;
		for (int i = 0; i < g.adjacencylist.size(); i++) {
			for (int p = 0; p < g.adjacencylist.get(i).size(); p++) {

				j = g.adjacencylist.get(i).get(p).dest;
				if (j < i)
					continue;

				if (visited[i] > 0 && visited[j] == 0)
					visited[j] = visited[i];

				else if (visited[i] == 0 && visited[j] == 0) {
					no_of_comp++;
					visited[i] = visited[j] = no_of_comp;
				}

				else if (visited[j] > 0 && visited[i] == 0)
					visited[i] = visited[j];

				else if (visited[i] > 0 && visited[j] > 0) {
					if (visited[i] == visited[j])
						continue;
					else {
						int yo = visited[i];
						for (int k = 0; k < g.noofnodes; k++) {
							if (visited[k] == yo)
								visited[k] = visited[j];
						}
					}
				}

		/*		System.out.print(i+ " " + j + " : \t\t");
				for (int z = 0; z < g.noofnodes; z++)
					System.out.print(visited[z] + "  ");
				System.out.println();
		*/
			}
		}

		int i;

		System.out.print(g.ID + " : ");
		for (i = 0; i < g.noofnodes; i++)
			System.out.print(visited[i] + "  ");

		System.out.print("\n");

		for (i = 0; i < g.noofnodes - 1; i++) {
			if (visited[i] == visited[i + 1])
				;
			else
				break;
		}

		if (i == g.noofnodes - 1)
			return true;

		return false;
	}
}