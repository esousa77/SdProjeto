package cliente;

// declara canal, declara fila, publica na fila. 
import com.rabbitmq.client.*; // fábrica de conexões
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.Queue;
import com.rabbitmq.utility.Utility;

import consumer.QueueingConsumer;


public class Send 
{
	
	private String QUEUE_NAME = "basic"; // Nome default para NomeFila, acaso não tenha sido declarado
	protected ConnectionFactory factory; 
	private Connection cliente; 
	private Channel channel; 
	private int canalNumber; // número do canal criado associado ao Cliente
	QueueingConsumer consumer;
	
	/**
	 * Construtor teste para obter informações sobre o serviço. 
	 * @param userName Nome do usuário
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public Send(String userName) throws IOException, TimeoutException
	{

		factory = new ConnectionFactory(); // Instancia factory
		cliente = factory.newConnection(); // instancia cliente
		channel = cliente.createChannel(); //Cria canal para cliente

		factory.setUsername(userName);
		factory.setHost("localhost");
		factory.setPort(5672);
		consumer = new QueueingConsumer(this.channel);


		/* System.out.println("#######################################################");

        System.out.println("Host: " + factory.getHost());
        System.out.println("Nome de usuário (userName): " + factory.getUsername());
        System.out.println("Connection está protegida? (SSL) " + factory.isSSL());
        System.out.println("Propriedades CLIENTE: " + factory.getClientProperties());

        System.out.println("Número do CHANNEL: " + channel.getChannelNumber());
        System.out.println("CLIENTE IP: " + cliente.getAddress());
        System.out.println("CHANNEL is open?: " + channel.isOpen());

        System.out.println("Propriedades SERVER: " + cliente.getServerProperties());
        System.out.println("Nome do CLIENTE: "+ cliente.getClientProvidedName());
        cliente.setId(userName);
        System.out.println("ID CLIENTE: "+ cliente.getId());
        System.out.println("CLIENTE Está aberto?: "+ cliente.isOpen());
        System.out.println("SeqNumber publish: " + channel.getNextPublishSeqNo());
        System.out.println("#######################################################");*/

		//this.canalNumber = channel.getChannelNumber(); // Pega número do canal e atribui ao int canalNumber

		//cliente.abort();
	}

	/**
	 * Construtor alternativo onde não são passados o host e porta, sendo atribuídos 
	 * o padrão para host: "localhost" e para porta: 5672 (segundo API rabbitMQ)
	 * @param userName Nome do usuário a ser cadastrado.
	 * @param QUEUE_NAME Nome do canal associado ao cliente
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public Send(String userName, String QUEUE_NAME) throws IOException, TimeoutException
	{	
		//this.userName = new ArrayList();
		this.QUEUE_NAME = QUEUE_NAME;
		//setNomeFila(QUEUE_NAME); //especifica o nome da fila
		//msg = new Mensagem();
		factory = new ConnectionFactory(); // Instancia factory
		cliente = factory.newConnection(); // instancia cliente
		channel = cliente.createChannel(); //Cria canal para cliente
		//insertUserNameArray(userName); //Insere o nome do cliente no arraylist local. 
		//setUserName(userName); // Especifica nome do usuário na API
		factory.setUsername(userName);
		cliente.setId(userName); // Atribui ID ao Sender definido com o userName.
		this.canalNumber = channel.getChannelNumber(); // Pega número do canal e atribui ao int canalNumber
		factory.setHost("localhost");
		factory.setPort(5672);
		consumer = new QueueingConsumer(this.channel);
	}

	/**
	 * @param userName Nome do cliente
	 * @param host IP do host alvo
	 * @param porta Porta onde o host alvo escuta
	 * @param QUEUE_NAME Nome do canal associado ao cliente
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public Send(String userName, String host, int porta, String QUEUE_NAME) throws IOException, TimeoutException
	{	
		//this.userName = new ArrayList();
		setNomeFila(QUEUE_NAME); //especifica um nome para a fila
		//msg = new Mensagem(); //instancia mensagem
		factory = new ConnectionFactory(); // Instancia factory
		cliente = factory.newConnection(); // instancia cliente
		cliente.setId(userName); // Atribui ID ao Sender definido com o userName.
		channel = cliente.createChannel(); //Cria canal para cliente
		this.canalNumber = channel.getChannelNumber(); // Pega número do canal e atribui a canal number
		//insertUserNameArray(userName); //Insere o nome do cliente no arraylist local. 
		setUserName(userName); // Especifica nome do usuário na API
		configHost(host); //Configura o host alvo
		configPorta(porta); // Configura a porta alvo
		consumer = new QueueingConsumer(this.channel);
	}

	/**
	 * Consome mensagens de usuários (nmFila);
	 * Nome da fila é o nome do Usuário na class Mensagem;
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws ConsumerCancelledException 
	 * @throws ShutdownSignalException 
	 */
	public void consumeMsg(String nmFila) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException
	{
		channel.basicConsume(nmFila, this.consumer);

		//Queue.DeclareOk response = channel.queueDeclarePassive(nmFila);
		// returns the number of messages in Ready state in the queue
		//response.getMessageCount();
		// returns the number of consumers the queue has
		//response.getConsumerCount();		
		while (true) 
		{
			QueueingConsumer.Delivery delivery = this.consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(" [x] Mensagens anteriores: '" + message + "'");  

		}
	}
	
	/**
	 * Método que consume mensagens de sua fila.
	 * @param Recebe o nome da fila, tipo String que representa o usuário e consome as mensagens em sua fila. 
	 * @return void
	 */
	public void receptorTypeDefault(String QUEUE_NAME) throws Exception 
	{
		//(queue-name, durable, exclusive, auto-delete, params); 
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		System.out.println("\n...");

		Consumer consumer = new DefaultConsumer(channel) 
		{
			public void handleDelivery(String consumerTag, Envelope envelope, 
					AMQP.BasicProperties properties, byte[] body) throws IOException 
			{
				String message = new String(body, "UTF-8");
				//Formatar data
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			    LocalDateTime myDateObj = LocalDateTime.now(); 
			    String formattedDate = myDateObj.format(myFormatObj);
			    
				System.out.println("(" + formattedDate + ") " + QUEUE_NAME + " diz: '" + message + "'");
			}
		};
		//(queue-name, autoAck, consumer);    
		channel.basicConsume(QUEUE_NAME, true, consumer);
	}
	
	/**
	 * Passado o nome do Exchange, as mensagens enviadas são consumidas. 
	 * @param EXCHANGE_NAME
	 * @throws Exception
	 */
	public void consomeMsgExchange(String EXCHANGE_NAME) throws Exception 
	{
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		String QueueName = channel.queueDeclare().getQueue();
		channel.queueBind(QueueName, EXCHANGE_NAME, "");

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x] Msg Recebida: '" + message + "'");
		};
		channel.basicConsume(QueueName, true, deliverCallback, consumerTag -> { });
	}
	
	/**
	 * Cria exchange com nome passado pelo usuário.
	 * @param EXCHANGE_NAME
	 * @throws IOException
	 */
	public void criaExchange(String EXCHANGE_NAME) throws IOException
	{		
		        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
	}
	
	/**
	 * Publica mensagem de um usuário no Exchange. São passados, 
	 * o nome do grupo e a mensagem como parâmetros
	 * @param EXCHANGE_NAME
	 * @param message
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void publicaEmExchange(String EXCHANGE_NAME, String message) 
			throws UnsupportedEncodingException, IOException
	{
		        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
		        System.out.println(" [x] Sent '" + message + "'");
	}
	
	/**
	 * Liga fila -queue- ao exchange. A ação é denominada bind. 
	 * @param queueName nome da fila.
	 * @param exchangeNome nome do exchange
	 * @throws IOException
	 */
	public void bindUserExchange(String queueName, String exchangeNome) throws IOException
	{
		channel.queueBind(queueName, exchangeNome, "");
	}
	
	//obter número do canal
	public int getChannelNumber()
	{
		return channel.getChannelNumber();
	}

	//mensagem de teste
	public String msg()
	{
		String str = "mensagem inicial de teste";
		return str;
	}

	/**
	 * 
	 * @return número de sequência de publicação
	 */
	public long getFila()
	{
		return (channel.getNextPublishSeqNo());
	}

	/**
	 * Cria, efetivamente, a fila com o QUEUE_NAME setado na instanciação da classe.  
	 * @param QUEUE_NAME Nome que atribuído a fila
	 * @throws IOException
	 */
	public void declararFila() throws IOException
	{
		channel.queueDeclare(this.QUEUE_NAME, false, false, false, null);	
	}

	/**
	 * Sobrecarrega o método declararFila(), com o nome da fila atribuído pelo usuário.   
	 * @param QUEUE_NAME Nome que atribuído a fila pelo usuário;
	 * @throws IOException
	 */
	public void declararFila(String QueueName) throws IOException
	{	
		channel.queueDeclare(QueueName, false, false, false, null);	
	}

	/**
	 * @param NomeCanal Nome atribuído ao canal
	 * @param message Mensagem a ser publicada no canal.
	 * @throws IOException
	 */
	public void publicarNaFila(String message) throws IOException
	{
		channel.basicPublish("", this.QUEUE_NAME, null, message.getBytes("UTF-8"));	
	}

	/**
	 * Sobrecarrega o método publicarNaFila(), onde se pode colocar o nomeda fila. 
	 * @param NomeCanal Nome atribuído ao canal
	 * @param message Mensagem a ser publicada no canal.
	 * @throws IOException
	 */
	public void publicarNaFila(String message, String QueueNome) throws IOException
	{
		channel.basicPublish("", QueueNome, null, message.getBytes("UTF-8"));	
	}

	/**
	 * Cria uma conexão (Cliente); 
	 * factory é um objeto do tipo Connection. Cada conexão está associada a um Cliente (sender).
	 * @param nome do usuário
	 */
	private void setUserName(String nome)
	{
		factory.setUsername(nome);  
	}

	/**
	 * Obtêm o nome do usuário. 
	 * @return nome de usuário;
	 */
	public String getUserName()
	{
		return factory.getUsername();	
	}

	/**
	 * Adiciona  Nome do usuário ao arrayList userName;
	 * @param userName (nome do usuário)
	 */
	/*	private void insertUserNameArray(String userName)
	{
		this.userName.add(userName);
	}*/

	/**
	 * Atribui nome a fila, mas não a cria. 
	 * @param QUEUE_NAME (nome da fila)
	 */
	public void setNomeFila(String QUEUE_NAME)
	{
		this.QUEUE_NAME = QUEUE_NAME; 
	}

	/**
	 * @return Obtêm nome da fila. 
	 */
	public String getNomeFila()
	{
		return this.QUEUE_NAME;
	}

	/**
	 * Configura o IP do host alvo.
	 * @param hostIP
	 */
	private void configHost(String hostIP)
	{
		factory.setHost(hostIP);
	}

	public String getQUEUE_NAME() 
	{
		return QUEUE_NAME;
	}

	public void setQUEUE_NAME(String qUEUE_NAME) 
	{
		QUEUE_NAME = qUEUE_NAME;
	}

	/**
	 * Configura a porta do host. 
	 * @param porta
	 */
	private void configPorta(int porta)
	{
		factory.setPort(porta);
	}

	/**
	 * Imprime Nome do usuário, IP e porta do host.
	 * @return info Informações sobre o usuário. 
	 */
	public String toString()
	{
		String info = "Username: " + factory.getUsername() + "\nHost: " + factory.getHost() + 
				"\nPorta: " + factory.getPort() + "ID cliente: " + cliente.getId(); 	
		return info;
	}

	public QueueingConsumer getConsumer() 
	{
		// TODO Auto-generated method stub
		return this.consumer;
	}
	
	public Channel getChannel() 
	{
		return channel;
	}

	public void setChannel(Channel channel) 
	{
		this.channel = channel;
	}
	
	public ConnectionFactory getFactory() 
	{
		return factory;
	}

	public void setFactory(ConnectionFactory factory)
	{
		this.factory = factory;
	}


}
