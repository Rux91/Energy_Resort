package behaviours;

import agents.BaseAgent;
import agents.BatteryAgent;
import agents.AeolianAgent;
import agents.DsoAgent;
import agents.SolarAgent;
import agents.BungalowAgent;
import behaviours.AeolianBehaviour;
import behaviours.SolarBehaviour;
import behaviours.ReceiveAeolianBungalow;
import behaviours.ReceiveSolarBungalow;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 * Behaviour per la ricezione dei messaggi da parte dei Bungalow.
 * 
 * Gestisce la ricezione dei messaggi con diversi Conversation ID e diverse
 * performative e per ogni tipo di messaggio richiama il behaviour apposito.
 */

public class ReceiveMessages extends TickerBehaviour {

	public ReceiveMessages(Agent a, long period) {
		super(a, period);
	}

	protected void onTick() {
		try {
			MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage msg = this.myAgent.receive(template);
			if (msg != null) {

				if (this.myAgent instanceof BungalowAgent) {

					if (msg.getConversationId().equals("priceaeolian")) {
						
						this.myAgent.addBehaviour(new ReceiveAeolianBungalow(msg));	
					} 
					else if (msg.getConversationId().equals("pricesolar")) {
						
						this.myAgent.addBehaviour(new ReceiveSolarBungalow(msg));	
					}
					else if (msg.getConversationId().equals("pricedso")) {
						
						this.myAgent.addBehaviour(new ReceiveDsoBungalow(msg));	
					} 
					else if (msg.getConversationId().equals("pricebattery")) {
						
						this.myAgent.addBehaviour(new ReceiveBatteryBungalow(msg));
					} 
					else if (msg.getConversationId().equals("stopselling")) {
						
						System.out.println(this.myAgent.getLocalName() + ": " + 
					            msg.getSender().getLocalName() + " dice: "+msg.getContent());  
					} 
					else if (msg.getConversationId().equals("Finished")) {
						
						System.out.println(this.myAgent.getLocalName() + ": " + msg.getSender().getLocalName()
								+ " dice: " + msg.getContent());
					} 
					else if (msg.getConversationId().equals("WakeUp")) {

						this.myAgent.addBehaviour(new ResearchProviderBehaviour(msg));

					}

				}
			}
			ACLMessage msg1 = this.myAgent
					.receive(MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
							(MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL))));
			if (msg1 != null) {

				if (this.myAgent instanceof BungalowAgent) {

					if (msg1.getConversationId().equals("AnswerToClient")) {
						this.myAgent.addBehaviour(new ManageSellingBehaviour(msg1));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}