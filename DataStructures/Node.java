package DataStructures;
import java.util.Vector;

import Math.*;
import World.Room;

public class Node 
{
    public Node pi; //padre del nodo
    public int d; //valore del nodo dato dal relax, studiando gli archi

    public Node[] neighbors; //nodi vicini
    public int[] archValuesList; //valori degli archi che connettono l'iesimo elemento di neightbors al Nodo principale
                             //quindi ad ogni i-archValues corrisponde il suo i-neightbors.
    public int neighborsCount;

    public Vector2 coordinates;

    public Node(Vector2 onRoomPosition)
    {
        neighbors = new Node[4]; //al più 4 vicini
        archValuesList = new int[4]; 

        coordinates = onRoomPosition;
        pi = null;
        d = 0;
        neighborsCount = 0;
    }
}
