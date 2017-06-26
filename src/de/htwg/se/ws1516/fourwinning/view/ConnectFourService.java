package de.htwg.se.ws1516.fourwinning.view;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.IncomingConnection;
import akka.http.javadsl.model.ws.Message;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.*;
import akka.Done;
import akka.http.javadsl.model.headers.HttpCredentials;
import akka.japi.function.Function;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import akka.util.ByteString;
import de.htwg.se.ws1516.fourwinning.models.*;
import de.htwg.se.ws1516.fourwinning.controller.IGameController;
public class ConnectFourService{
    IGameController spiel;
    final Function<HttpRequest, HttpResponse> requestHandler;
    Source<IncomingConnection, CompletionStage<ServerBinding>> serverSource;
    final Materializer materializer;

    public ConnectFourService(IGameController spiel) {
        this.spiel = spiel;
        ActorSystem system = ActorSystem.create();
        materializer = ActorMaterializer.create(system);

        serverSource =
                Http.get(system).bind(ConnectHttp.toHost("localhost", 8080), materializer);

        requestHandler =
                new Function<HttpRequest, HttpResponse>() {
                    private final HttpResponse NOT_FOUND =
                            HttpResponse.create()
                                    .withStatus(404)
                                    .withEntity("Unknown resource!");

                    @Override
                    public HttpResponse apply(HttpRequest request) throws Exception {
                        Uri uri = request.getUri();
                        if (request.method() == HttpMethods.GET) {
                            if (uri.path().equals("/"))
                                return
                                        HttpResponse.create()
                                                .withEntity(ContentTypes.TEXT_HTML_UTF8,
                                                        "<html><body>Type show into the URI to display the game! (localhost:8080/show)</body></html>");
                            else if (uri.path().equals("/hello")) {
                                String name = uri.query().get("name").orElse("Mister X");

                                return
                                        HttpResponse.create()
                                                .withEntity("Hello " + name + "!");
                            } else if (uri.path().equals("/show")) {

                                Feld[][] feld = spiel.update();
                                int rows = spiel.getRows();
                                int columns = spiel.getColumns();
                                Player eins = spiel.getPlayerOne();
                                Player zwei = spiel.getPlayerTwo();
                                StringBuilder sb = new StringBuilder();
                                String NEWLINE = System.getProperty("line.separator");
                                sb.append(NEWLINE);
                                for (int k = 0; k < rows; k++) {
                                    for (int l = 0; l < columns; l++) {
                                        if (feld[k][l].getSet()) {
                                            if (feld[k][l].getOwner().getName().equals(eins.getName())) {
                                                sb.append("[X]\t");

                                            } else if (feld[k][l].getOwner().getName().equals(zwei.getName())) {
                                                sb.append("[O]\t");

                                            }
                                        } else {
                                            sb.append("[ ]\t");

                                        }
                                    }
                                    sb.append(NEWLINE);
                                }
                                String ausgabe = sb.toString();
                                String result = ausgabe.replace(NEWLINE, "<br>");
                                result = result.replace("%n", "<br>");
                                result = result.replace("     ", " &nbsp; &nbsp; ");
                                result = result.replace("   ", " &nbsp; ");
                                return HttpResponse.create()
                                        .withEntity(ContentTypes.TEXT_HTML_UTF8,
                                                "<html><body>"+result+"</body></html>");
                            }
                            else if (uri.path().equals("/ping"))
                                return HttpResponse.create().withEntity("PONG!");
                            else
                                return NOT_FOUND;
                        }
                        return NOT_FOUND;
                    }

                };

        CompletionStage<ServerBinding> serverBindingFuture =
                serverSource.to(Sink.foreach(connection -> {
                    System.out.println("Accepted new connection from " + connection.remoteAddress());

                    connection.handleWithSyncHandler(requestHandler, materializer);
                })).run(materializer);
    }
}