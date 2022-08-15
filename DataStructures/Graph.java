package DataStructures;

import java.util.ArrayList;
import java.util.List;

import Main.Main;
import Main.Utils;
import Math.RectInt;
import Math.Vector2;
import World.Map;
import World.Room;
import World.Room.RoomType;


public class Graph 
{

    
    public Node[] unconnectedNodeMatrix; //grafo senza archi relativo alla stanza
    public Node[] V; //grafo effettivamente connesso (nome legacy)

    public int Vcount; //numero di nodi

    public Graph(Room room) //essendo una funzione srtutturata, l'argomento non è generico ma specifico al problema
    {
        //init
        int width = room.bounds.width;
        int height = room.bounds.height;
        unconnectedNodeMatrix = new Node[width * height];

        int Vcount = 0;

        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                int offset = i * width +j;
                //first thing first, escludiamo i lati:
                if(i == 0 || i == (height - 1) || j == 0 || j == (width - 1))
                {
                    //non importa se sono ancora muri o sono passaggi abbattuti dall'utente, non li considero
                    continue;
                }

                if(room.tiles[offset] != null)
                {
                    if(room.tiles[offset].collision)
                    {
                        //la tile è un ostacolo quindi va ignorata
                        continue;
                    }

                    Node newNode = new Node(new Vector2(j, i));
                    unconnectedNodeMatrix[offset] = newNode;
                    Vcount++;
                }
            }
        }

        /*a questo punto quello che devo fare è creare il grafo con liste di adiacenza attraverso la matrice 
          appena creata, che avrà un nodo negli offset walkable e NULL in quelli non camminabili*/

        V = new Node[Vcount];
        this.Vcount = Vcount;        

        int vListCounter = 0;
        List<Node> usedNode = new ArrayList<Node>();

        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {

                int offset = i * width + j;
                if(unconnectedNodeMatrix[offset] == null || i == 0 || i == (height - 1) || j == 0 || j == (width - 1))
                {
                    continue;
                }

                Node node = unconnectedNodeMatrix[offset];
                
                V[vListCounter] = node;
                vListCounter++;
                for(int k = 0; k < Utils.allDirections.length; k++)
                {
                    Vector2 unitVector = Vector2.directionsVector[k];
                    int perimeterOffset = (node.coordinates.y + unitVector.y) * width + (node.coordinates.x + unitVector.x);

                    if(unconnectedNodeMatrix[perimeterOffset] != null)
                    {
                        node.neighbors[node.neighborsCount] = unconnectedNodeMatrix[perimeterOffset];

                        int randomWeight = Main.rand.nextInt(10);
                        int weight = 10 + randomWeight; //Math.round(1 + randomWeight);
                        node.archValuesList[node.neighborsCount] = weight;
                        node.neighborsCount++;

                    }
                    
                }
                usedNode.add(node);
            }
        }

    }

    public static void InitializeSingleSource(Graph G, Node s)
    {
        for(int i = 0; i < G.V.length; i++)
        {
            G.V[i].d = Integer.MAX_VALUE; //sarebbe infinito
            G.V[i].pi = null; //nessun padre per adesso
        }

        //per ultimo, la source, ovvere il nodo da cui partono tutti i cammini, avrà peso = 0
        s.d = 0;
    }

    public static void Relax(Node u, Node v, int w)
    {
        //NB, w è il peso dell'arco che unisce u e v, quindi
        if(v.d > u.d + w)
        {
            v.d = u.d + w;
            v.pi = u;
        }

        /* siccome utilizzo variabili "matematiche" è bene spiegare un minimo cosa accade qui: 
         * nella teoria dei grafi, il processo di "rilassamento" di un arco consiste nel verificare per ogni nodo v se, 
         * passando per l'arco che unisce v ad u, è possibile migliorare il cammino.
         * se infatti, il peso di v è maggiore del peso di u PIU' l'arco che li unisce, allora il cammino non è ottimizzato, 
         * e va quindi cambiato, aggiornando i parametri di v al nuovo peso migliorato e con il suo nuovo "padre".
         */
    }

    public static void Dijkstra(Graph G, Node s)
    {
        InitializeSingleSource(G, s);
        
        Node[] Q = new Node[G.V.length];

        for(int i = 0; i < G.V.length; i++)
        {
            Q[i] = G.V[i];
        }

        while(!Queue.minNodeQueueIsEmpty(Q))
        {
            Node u = Queue.extractMin(Q);
            for(int i = 0; i < 4 && u.neighbors[i] != null; i++)
            {
                Relax(u, u.neighbors[i], u.archValuesList[i]);
            }
        }

        /* L'algoritmo di Dijkstra si basa sulla teoria del rilassamento degli archi (e dimostrato da lui stesso) la quale specifica che:
         * se per ogni vertice si rilassa ogni suo arco, allora, per definizione vk.d = d(s, vk), ovvero il cammino è il cammino minimo.
         * questo per ogni cammino p = (v0, v1, ... ,vk)
        */ 

        return; 
    }
}
