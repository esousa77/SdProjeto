package mensagem;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

import cliente.Send;

//###################################
public class Mensagem
{
	protected Scanner sc;
	protected Send send;
	protected String nomeUser;
	protected static boolean TERMINATE = false;
	protected String grupo;
	protected String data = "19/05/2019";
	protected String hora = "agora";
	protected Date d1;

	public Mensagem() throws IOException, TimeoutException
	{
		sc = new Scanner(System.in);
		d1 = new Date();
		send = new Send(nomeUser);
	} 

	public void menu()
	{
		int opt;
		String option;

		System.out.println("Escolha uma opção: ");
		System.out.println("Ver filas existentes: 1 ");
		System.out.println("Deletar fila: 2 ");
		System.out.println("Ver número de mensagens na fila: 3 ");
		opt = sc.nextInt();

		switch(opt)
		{
		case 1:
			option = "Ver filas";
			break;
		case 2:
			option = "Deleter filas";
			break;
		case 3:
			option = "Ver número msg na fila";
			break;
		case 4:
			option = "Criar grupo";
			break;
		case 5:
			option = "Adicionar User ao grupo";
			break;	
		case 6:
			option = "sair";
			break;
		case 7:
			option = "Deletar grupo";
			break;
		default: 
			option = "Valor não está correto";
		}
		System.out.println(option);
	}
	/**
	 * Obtêm usuário a partir do cmd "@nmUsr".
	 * @param msg Mensagem do usuário
	 * @return nmUsr Nome do Usuário.
	 */
	public String getUsr(String msg)
	{
		String nmUsr = msg.substring(1);
		return nmUsr;
	}
	
	/**
	 * Obtêm grupo a partir do cmd "!addGrupo nmGrupo".
	 * @param msg Mensagem do usuário
	 * @return nmGrupo Nome do grupo.
	 */
	public String getGrupo(String msg)
	{
		String[] ss = msg.split(" ");
		//String nmComd = ss[0];
		String nmGrupo = ss[1];
		
		return nmGrupo;
	}
	/**
	 * Obtêm usr a partir do cmd "!addUserGrupo nmUsr nmGrupo".
	 * @param msg mensagem do usuário
	 * @return nmUser
	 */
	public String getnmUsrInsertGrupo(String msg)
	{
		String[] ss = msg.split(" ");
		//String nmComd = ss[0];
		String nmUser = ss[1];
		//String nmGrupo = ss[2];
		
		return nmUser;
	}
	
	/**
	 * Obtêm usr a partir do cmd "!addUserGrupo nmUsr nmGrupo".
	 * @param msg mensagem do usuário
	 * @return nmGrupo
	 */
	public String getnmGrupoInsertGrupo(String msg)
	{
		String[] ss = msg.split(" ");
		//String nmComd = ss[0];
		//String nmUser = ss[1];
		String nmGrupo = ss[2];
		
		return nmGrupo;
	}
	
	/**
	 * Método para avaliar qual comando o usuário deseja executar. As strings esperadas tem a forma 
	 * 1: "!addUser nmUser nmGrupo", 2: "!addGrupo nmGrupo", 3:"@nmUser nmuser".
	 * @param msgUser String a ser avaliada.
	 * @return String[] ss com os elementos da String identificados através da separação por espaços.  
	 */
	public String avalCmd(String msgUser)
	{
		boolean avalCmdUsr = msgUser.substring(0).contains("@");
		boolean avalCmdGroup = msgUser.contains("!addGrupo ");
		boolean avalCmdUserGrupo = msgUser.contains("!addUser ");	

		if(avalCmdGroup)
		{	
			return "!addGrupo ";
		}
		if(avalCmdUserGrupo)
		{
			return "!addUserGrupo ";
		}
		if(avalCmdUsr)
		{
			return "paraUsr ";
		}
		else
		{
			return "CmdNotIdentified ";
		}			
	}

	/**
	 * Método que verifica se determinada msg é um Cmd. 
	 * @param msg Mensagem do usuário
	 * @return booleano com valor true para Cmd ou false, caso contrário. 
	 */
	public boolean isCmd(String msg)
	{
		boolean flag = false;
		if(msg.substring(0).contains("!") || msg.substring(0).contains("@") || msg.substring(0).contains("#"))
		{
			flag = true;
		}else
		{
			flag = false;
		}
		return flag;
	}
//#######################################################
	//Método mensagem....
	/**
	 * método mensagem que contém a lógica de negócio do chat
	 * @throws Throwable
	 */
	public void msg() throws Throwable
	{   
		//mensagens dos usuários;
		String message = " inicializando chat";

		System.out.print("Digite o nome do Usuário: \n");
		nomeUser = sc.nextLine(); 

		//System.out.printf("O usuário @%s logou ", this.nomeUser);
		System.out.printf("\nBem vindo @%s! ", this.nomeUser);
		System.out.printf("("+ String.format("%tr", d1) + ")");

		// declara fila com nome igual ao nome do Usuário digitado. 
		send.declararFila(nomeUser); 

		// Termina inicialização do chat
		//########################################################
		//Inicia loop - chat

		while(!TERMINATE)
		{
			//Imprime cursor formatado
			System.out.printf("\n@%s>> ", this.nomeUser);
			//Obtêm a mensagem do usuário.
			message = sc.nextLine();
			
			//Inicia fluxo alternativo se msg contiver comando...
			if(isCmd(message) == true)
			{
				if(avalCmd(message).equals("!addGrupo "))
					//cria um grupo - criaExchange().
					//Obtêm nome do grupo a partir da mensagem. O comando esperado é !addGrupo
				{	
					//Obtêm nome do grupo a partir da mensagem e cria o exchange.
					try 
					{
						//String nmGrupo = message.substring(cGrupo);
						send.criaExchange(getGrupo(message));
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}	
				}
				if(avalCmd(message).equals("!addUserGrupo "))
					//vincula usuário ao grupo (Exchange). 
					//O formato da mensagem é "!addUser nmUser nmGrupo"; 
				{		
					//Obtêm nome do usuário a ser adcionado ao exchange.
					//A ideia é: "!addUser ".length compreende 0-8, o user inicia em 9. 
					//String nmUser = message.substring(cUser);

					//System.out.println("Digite nome do Grupo...");
				    //String nmGrupo = sc.next();
					
					//adiciona ao exchange
					send.bindUserExchange(getnmUsrInsertGrupo(message), getnmGrupoInsertGrupo(message)); 		 
				}
				if(avalCmd(message).equals("paraUsr "))
					//publica na fila de um usuário
				{
					//Obtêm nome do usuário da mensagem
					String QueueNome = message.substring(1);

					while(!message.toLowerCase().equals("exit"))
					{
						System.out.printf("\n@%s>> ", QueueNome);
						System.out.println("");
						message = sc.next();
						//O nome do usuário é o nome da fila. Publica a mensagem na fila. 
						send.publicarNaFila(message, QueueNome);
					}
				}
		/*		if(( avalCmd(message).toLowerCase().contains("parausr")) && 
						(message.toLowerCase().substring(1) == "options"))
				{

				}*/			
			}
			else
			{
				if(message.equals("exit"))
			    //Sair do chat se usr digitar a quit
				{
						TERMINATE = true; 
						System.out.printf("\nO usuário(a) @%s deslogou! \n", nomeUser);	
				}
				//Consome mensagens da fila do usuário.
				send.receptorTypeDefault(nomeUser);

				//Publicar na fila criada com o nome do usuário. 
				send.publicarNaFila(message, nomeUser);		
			}
		}

	}
}