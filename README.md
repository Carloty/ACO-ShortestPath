# English version

## Small description
Java implementation of the ACO Shortest Path algorithm

## How to use this software
Compile source code and launch Software class (main class).

## Explanation of the assumed use of this software
TODO

# Version française

## Courte description
Ce logiciel Java simule le fonctionnement d'un algorithme de type ACO (Ant Colony Optimization), qui a pour but de trouver le plus court de chemin entre deux points.

## Comment utiliser ce logiciel
Compiler et lancer la classe Software (classe principale).

## Explication du fonctionnement supposé du logiciel
Ce logiciel a pour but de simuler le fonctionnement de l'intelligence collective des fourmis, leur permettant de trouver, par exemple, le plus court chemin entre la fourmilière et la nourriture.

Pour le faire fonctionner correctement, il est nécessaire d'indiquer l'emplacement de la fourmilière en activant le radio bouton associé et en cliquant dans la zone blanche à l'emplacement désiré.

Il est nécessaire de faire de même avec l'emplacement de la nourriture et les différents "point étape" qui vont servir de support aux chemins possibles.

Les points doivent ensuite être reliés, soit en cliquant sur le bouton "relier les étapes", soit en activant le radio bouton "Chemin" et en cliquant sur la position de début et ensuite de fin du chemin que l'on souhaite créer, sachant qu'un chemin ne peut se créer qu'entre deux point déjà existants.

Il est ensuite possible de choisir le nombre de fourmis composant la fourmilière, ainsi que le nombre d'aller-retour qu'elles feront jusqu'à la fin de la simulation.

Le bouton "Simuler !" permet finalement de lancer la simulation.

## Eléments visualisables
Après le lancement de la simulation, le nombre d'itérations effectuées (c'est-à-dire le nombre d'aller-retour effectués par toutes les fourmis) est affiché.

Les chemins s'affichant en bleu clair correspondent aux chemins choisis par les fourmis.

A la fin de la simulation, un chemin s'affiche en rose. Il correspond au plus court chemin trouvé par la colonie de fourmis. 
La probabilité que les fourmis empruntent ce chemin en sortant de la fourmilière s'affiche également en rose, c'est un nombre entre 0 et 1.
