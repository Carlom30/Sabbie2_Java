package DataStructures;

public class Queue 
{
    public Node[] array;

    public int head;
    public int tail;

    public Queue(int length)
    {
        array = new Node[length];
        head = 0;
        tail = 0;
    }

    public void enqueue(Node x)
    {
        array[tail] = x;
        if(tail == array.length - 1)
        {
            tail = 0;
            
        }

        else
        {
            tail++;
        }
    }

    public Node dequeue()
    {
        Node x = array[head];
        array[head] = null;

        if(head == array.length - 1)
        {
            head = 0;
        }

        else
        {
            head++;
        }

        return x;
    }

    public boolean isEmpty()
    {
        for(int i = 0; i < array.length; i++)
        {
            if(array[i] != null)
                return false;
        }

        return true;
    }
    //dead code up
    //la roba sopra è per la BFS che però non uso perché troppo precisa

    public static boolean minNodeQueueIsEmpty(Node[] Q)
    {
        for(int i = 0; i < Q.length; i++)
        {
            if(Q[i] != null)
                return false;
        }

        return true;
    }

    public static Node extractMin(Node[] Q)
    {
        //a bit hardcoded yeah but still linear cost
        Node minNode = null;
        int j = 0;
        //take the first element whom is not null
        for(int i = 0; i < Q.length; i++)
        {
            if(Q[i] != null)
            {
                minNode = Q[i];
                j = i;
                break;
            }
        }

        //then form the j looks for the smallest
        for(int i = 0; i < Q.length; i++)
        {
            if(Q[i] != null)
            {
                if(minNode.d > Q[i].d)
                {
                    minNode = Q[i];
                    j = i;
                }
            }
        }

        Q[j] = null;
        return minNode;
    }
    //try for git
}
