Per avviare il programma navigare nella cartella dove si è scaricato l'eseguibile .jar ed utilizzare da terminale il comando

```
java -jar AStar.jar
```

A questo punto ci si trova davanti ad una schermata con tre scelte:
1. per usare l'algoritmo A* per un certo numero di esperimenti su un grafo letto da due file di testo
2. per usare l'algoritmo A* su grafi creati con il modello G(n,p) di Erdős e Rényi. Viene generato un nuovo grafo per ogni istanza di esperimento
3. per generare un grafo con il modello di Erdős e Rényi. Questo viene salvato in due file di testo "Nodes_Erdos_Renyi_{numeroNodi}V_{probabilità}P.txt" e "Edges_Erdos_Renyi_{numeroArchi}E_{probabilità}P.txt"

I file di testo usati nella prima opzione e generati dalla terza sono così formattati:

Nodes.txt
```
{IDNodo} {CoordinataX} {CoordinataY}
```

Edges.txt
```
{IDArco} {NodoDiPartenza} {NodoDiArrivo} {Peso}
```