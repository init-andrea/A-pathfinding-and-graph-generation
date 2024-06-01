Il progetto è un'implementazione dell'algoritmo di pathfinding A*, corredato dalla generazione di grafi secondo il modello di Erdős e Rényi.

Per avviare il programma navigare nella cartella dove si è scaricato l'eseguibile .jar ed utilizzare da terminale il comando

```
java -jar AStar.jar
```

A questo punto ci si trova davanti ad una schermata con tre scelte:
1. per usare l'algoritmo A* per un certo numero di esperimenti su un grafo letto da due file di testo
2. per usare l'algoritmo A* su grafi creati con il modello G(n,p) di Erdős e Rényi. Viene generato un nuovo grafo per ogni istanza di esperimento
3. per generare un grafo con il modello di Erdős e Rényi. Questo viene salvato in due file di testo "Nodes_Erdos_Renyi_{numeroNodi}V_{probabilità}P.txt" e "Edges_Erdos_Renyi_{numeroArchi}E_{probabilità}P.txt"

I file di testo da usare nella prima opzione devono stare nella stessa cartella di AStar.jar ed essere così formattati:

Nodes.txt
```
{IDNodo} {CoordinataX} {CoordinataY}
```
ogni riga rappresenta un nodo con il suo ID e le sue coordinate X e Y. I tre attributi sono separati da uno spazio

Edges.txt
```
{IDArco} {NodoDiPartenza} {NodoDiArrivo} {Peso}
```
ogni riga rappresenta un arco con il suo ID, nodi di partenza e arrivo e peso. I quattro attributi sono separati da uno spazio

I file generati dalla terza opzione seguono questa formattazione e vengono generati già nella cartella di AStar.jar. Possono quindi essere utilizzati senza necessità di modifiche.

Per continuare bisogna scrivere il numero relativo alla modalità che si intende utilizzare e premere invio. Inserire solo i numeri 1, 2 o 3.

Scegliendo l'opzione numero 1 viene chiesto di inserire il numero di esperimenti da eseguire, il nome dei file contenenti nodi ed archi del grafo (inserire anche l'estensione .txt alla fine) e la probabilità di generare un secondo arco.
La probabilità qui serve a passare da grafo orientato a non orientato se necessario (va da 0 per non aggiungere archi oltre a quelli nel file di testo a 1 per aggiungere ad ogni arco un corrispettivo orientato in senso opposto; ogni numero nel mezzo per aggiungere solo un {probabilità}% di archi).

A questo punto A* genera coppie casuali di nodi all'interno del grafo caricato e, se esiste, trova il percorso minimo che li collega.
Per ogni coppia di vertici viene stampato a terminale se è stato trovato un percorso e, nel caso ci sia, il costo totale, seguito dalla lista di nodi attraversati.

Al termine viene stampato il numero di esperimenti che hanno avuto successo con la percentuale relativa al numero totale di esperimenti eseguiti, il tempo totale di esecuzione del programma, il tempo medio per esperimento ed il tempo medio di A* per esperimento.
Infine vengono aggiornati (o creati nel caso in cui non esistano) i file con i risultati sia per l'intero algoritmo che per A*.

Scegliendo invece l'opzione numero 2 vengono chiesti il numero di esperimenti da eseguire, il numero di nodi dei grafi da generare e la probabilità di creare un arco tra due nodi.
In questo caso la probabilità ha un significato diverso, in quanto serve a scegliere la densità del grafo (va da 0 per un grafo sparso a 1 per un grafo denso). 
Una nota in merito alla probabilità è che al crescere del numero di nodi N si tenderà asintoticamente ad avere grafi connessi usando una probabilità $P=ln(n)/2n$.

Al termine della successiva esecuzione verranno stampate le stesse statistiche del caso precedente ed aggiornati i file con i risultati.

Infine con la terza opzione vengono chiesti il numero di nodi e probabilità per generare il grafo. Usando lo stesso modello dell'opzione precedente per la generazione del grafo la probabilità ha lo stesso significato.
Vengono quindi creati i file con nodi ed archi rappresentati il grafo generato.
