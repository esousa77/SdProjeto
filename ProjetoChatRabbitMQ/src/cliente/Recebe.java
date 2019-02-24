package cliente;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Envelope;

public class Recebe 
{
	Send send;
	private String userName;
	
	public Recebe() throws IOException, TimeoutException
	{
		send = new Send(this.userName);
	}

/*	public void consomeTypeExchange(String userName, String EXCHANGE_NAME) throws Exception 
	{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		String nmQueu = channel.queueDeclare().getQueue();
		channel.queueBind(userName, EXCHANGE_NAME, "");

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x] Received '" + message + "'");
		};
		channel.basicConsume(userName, true, deliverCallback, consumerTag -> { });
	}*/

	/*public void receptorTypeDefault(String QUEUE_NAME) throws Exception 
	{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri("amqp://...");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		//(queue-name, durable, exclusive, auto-delete, params); 
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		System.out.println(" [*] Esperando recebimento de mensagens...");

		Consumer consumer = new DefaultConsumer(channel) 
		{
			public void handleDelivery(String consumerTag, Envelope envelope, 
					AMQP.BasicProperties properties, byte[] body) throws IOException 
			{

				String message = new String(body, "UTF-8");
				System.out.println(" [x] Mensagem recebida: '" + message + "'");

				//(deliveryTag, multiple);
				//channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};
		//(queue-name, autoAck, consumer);    
		channel.basicConsume(QUEUE_NAME, true, consumer);
	}*/
}
