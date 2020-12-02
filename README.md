# RMI-BeginFlorian

## Code source :

RMI Poll Thread (Pas de commentaire de code) -> [RMIPollThread](https://github.com/F-Begin/RMI-BeginFlorian/tree/main/RMIPollThread).  
RMI Callback (Version la plus avancée, totalement fonctionnelle, commentaire de code) -> [RMI](https://github.com/F-Begin/RMI-BeginFlorian/tree/main/RMI).

## Fonction RMI Callback

1. Tout d'abord le serveur est démarré et ce dernier s'initialise.
2. Ensuite un client est démarré et va se connecter au serveur.
    1. Le client demande le nom d'utilisateur à utiliser (Pseudo ?).
    2. Le client s'ajoute à la liste des Listeners du serveur.
    3. Le serveur envoie un message à tout les autres utilisateurs pour notifier l'arrivée du nouveau.
    4. Le client ne reçoit **pas** d'autre indication, il rentre tout de suite dans une boucle qui lui permet d'ecrire les messages qu'il désire pour communiquer.
      Il ne recevra pas ses propres messages.
    5. Le client rentre `/quit` pour se déconnecter.
    6. Le serveur envoie un message à tout le monde pour notifier la déconnexion.
    7. Le client se retire de la liste des Listeners.
    8. Le client sort de la boucle et son programme se quitte tout seul.
3. Fin

##  Fonctionnement du Callback

1. L'utilisateur s'ajoute à la liste des Listeners serveur en utilisant la méthode implémentée côté serveur `addMessageListener()`.
2. Le serveur notifie tout les utilisateurs de la nouvelle connexion, il donne le nom du nouvel utilisateur en recupérant ce nom via la méthode `returnName()` implémentée côté
client.
3. L'utilsateur envoie un message en utilisant la methode implémentée côté serveur `sendMessage()`.
4. Le serveur reçoit le message et sa méthode `sendMessage()` invoque le méthode `notifyMsgListener()`
5. La méthode `notifyMsgListener()` va invoquer les méthodes `newMsg()` implémentées chez les clients, **sauf chez le clients qui a envoyé le message**.
6. La méthode `newMsg()` côté client va afficher le nouveau message.
7. Une fois terminé, le client s'enlevera de lui même de la liste des Listeners en invoquand la méthode `removeMessageListener()` implémentée côté serveur.
8. Le serveur notifie tout les utilisateurs de la deconnexion, il donne le nom de l'utilisateur concerné en recupérant ce nom via la méthode `returnName()` implémentée côté
client.

## Détails
1. Si un utilisateur quitte de manière non conventionnelle, ce dernier ne s'enlèvera pas de la liste des Listeners côté serveur, cela génèrera une exception lorsque le serveur
voudra notifier les clients. Cette exception est totalement gérée, le serveur enlèvera de lui même le client problématique de la liste des Listeners.
