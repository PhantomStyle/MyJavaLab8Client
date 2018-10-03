import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import static sun.misc.PostVMInitHook.run;

public class TestASClient {

    /**
     * @param args
     * @throws InterruptedException
     */

    //потом вынеси это в отдельный нестатический метод, чтообы можно было имя запомнить


    //передавай параметры


    private static String name = "kek";


    public TestASClient() throws IOException {
    }

    public static void main(String[] args) throws InterruptedException, IOException {

                Socket socket = new Socket("localhost", 3345);


                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                DataOutputStream oos = new DataOutputStream(socket.getOutputStream());


                DataInputStream ois = new DataInputStream(socket.getInputStream());


                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
// запускаем подключение сокета по известным координатам и нициализируем приём сообщений с консоли клиента      
//        try () {

            System.out.println("Client connected to socket.");
            System.out.println();
            System.out.println("Client writing channel = oos & reading channel = ois initialized.");
//            runPrinter(reader, ois);
//            runSender(socket, br, oos, ois);
            runAll(socket, br, oos, ois, reader);
        }

// проверяем живой ли канал и работаем если живой
//            new Runnable(){
//
//                @Override
//                public void run() {
//
//                }
//            }.run();
//            while (!socket.isOutputShutdown()) {
//
//// ждём консоли клиента на предмет появления в ней данных
//                if (br.ready()) {
//
//// данные появились - работаем
//                    System.out.println("Client start writing in channel...");
//                    Thread.sleep(1000);
//                    String clientCommand = br.readLine();
//
//// пишем данные с консоли в канал сокета для сервера
//                    clientCommand = name + ": " + clientCommand;
//                    oos.writeUTF(clientCommand);
//                    oos.flush();
////                    System.out.println(clientCommand);
//                    Thread.sleep(1000);
//// ждём чтобы сервер успел прочесть сообщение из сокета и ответить
//
//// проверяем условие выхода из соединения
//                    if (clientCommand.equalsIgnoreCase("quit")) {
//
//// если условие выхода достигнуто разъединяемся
//                        System.out.println("Client kill connections");
//                        Thread.sleep(2000);
//
//// смотрим что нам ответил сервер на последок перед закрытием ресурсов
//                        if (ois.read() > -1) {
//                            System.out.println("reading...");
//                            String in = ois.readUTF();
//                            System.out.println(in);
//                        }
//
//// после предварительных приготовлений выходим из цикла записи чтения
//                        break;
//                    }

// если условие разъединения не достигнуто продолжаем работу
//                    System.out.println("Client sent message & start waiting for data from server...");
//                    Thread.sleep(2000);

// проверяем, что нам ответит сервер на сообщение(за предоставленное ему время в паузе он должен был успеть ответить)
//                    if (ois.read() > -1) {
//
//// если успел забираем ответ из канала сервера в сокете и сохраняем её в ois переменную,  печатаем на свою клиентскую консоль
////                        System.out.println("reading...");
//                        String in = reader.readLine();
////                        String in = ois.readUTF();
//                        System.out.println(in);
//                    }


// на выходе из цикла общения закрываем свои ресурсы
//            System.out.println("Closing connections & channels on clentSide - DONE.");

//        } catch (UnknownHostException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }


    private static synchronized void runSender(Socket socket, BufferedReader br, DataOutputStream oos, DataInputStream ois) {
        new Thread() {

            @Override
            public void run() {
                while (!socket.isOutputShutdown()) {

// ждём консоли клиента на предмет появления в ней данных
                    try {
                        if (br.ready()) {

                            // данные появились - работаем
                            System.out.println("Client start writing in channel...");
                            Thread.sleep(1000);
                            String clientCommand = br.readLine();

                            // пишем данные с консоли в канал сокета для сервера
                            clientCommand = name + ": " + clientCommand;
                            oos.writeUTF(clientCommand);
                            oos.flush();
                            //                    System.out.println(clientCommand);
                            Thread.sleep(1000);
                            // ждём чтобы сервер успел прочесть сообщение из сокета и ответить

                            // проверяем условие выхода из соединения
                            if (clientCommand.equalsIgnoreCase("quit")) {

                                // если условие выхода достигнуто разъединяемся
                                System.out.println("Client kill connections");
                                Thread.sleep(2000);

                                // смотрим что нам ответил сервер на последок перед закрытием ресурсов
                                if (ois.read() > -1) {
                                    System.out.println("reading...");
                                    String in = ois.readUTF();
                                    System.out.println(in);
                                }

                                // после предварительных приготовлений выходим из цикла записи чтения
                                break;
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private static synchronized void runPrinter(BufferedReader reader, DataInputStream ois) {
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    try {
                        if (ois.read() > -1) {

                            // если успел забираем ответ из канала сервера в сокете и сохраняем её в ois переменную,  печатаем на свою клиентскую консоль
                            //                        System.out.println("reading...");
                            String in = reader.readLine();
                            //                        String in = ois.readUTF();
                            System.out.println(in);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private static synchronized void runAll(Socket socket, BufferedReader br, DataOutputStream oos, DataInputStream ois, BufferedReader reader){
        runSender(socket, br, oos, ois);
        runPrinter(reader, ois);
    }
}
