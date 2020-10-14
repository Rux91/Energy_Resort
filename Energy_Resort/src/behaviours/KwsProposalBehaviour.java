package behaviours;

import agents.AeolianAgent;
import agents.BaseAgent;
import agents.BatteryAgent;
import agents.SolarAgent;
import data.BungalowData;
import database.DbAeolianData;
import database.DbBatteryData;
import database.DbSolarData;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class KwsProposalBehaviour extends OneShotBehaviour {

	BungalowData msgBungalowData;
	ACLMessage msg;

	public KwsProposalBehaviour(Agent a) {
		super(a);
	}

	public KwsProposalBehaviour(ACLMessage msg) {
		try {
			this.msg = msg;
			msgBungalowData = (BungalowData) msg.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
	}

	public void action() {
		if (this.myAgent instanceof SolarAgent) {
			try {
				DbSolarData solarDb = ((SolarAgent) myAgent).getDbSolar();
				String senderName = msg.getSender().getLocalName();
				String localSolar = this.myAgent.getLocalName();
				
				
				if (solarDb.getMyKw(localSolar) <= solarDb.getConsumerEnReq(senderName)) {

					new BaseAgent().sendMessageToAgentsByServiceType(this.myAgent, msg.getSender(), "Finished",
							"Vendo a te " + (solarDb.getMyKw(localSolar)
							+ " Kw."));

					solarDb.updateConsumerData(
							solarDb.getConsumerEnReq(senderName)
							- solarDb.getMyKw(localSolar),
							solarDb.getConsumerBudget(senderName)
							- ((SolarAgent) myAgent).getSolar().getSolarPrice()
							* solarDb.getMyKw(localSolar),
							senderName);

					solarDb.updateProviderData(0, ((SolarAgent) myAgent).getSolar().getSolarPrice(), "Solar");

				} else {
					new BaseAgent().sendMessageToAgentsByServiceType(this.myAgent, msg.getSender(), "Finished",
							"Vendo a te " + solarDb.getConsumerEnReq(senderName) + " Kw.");

					
					solarDb.updateProviderData(solarDb.getMyKw(localSolar)-solarDb.getConsumerEnReq(senderName), ((SolarAgent) myAgent).getSolar().getSolarPrice(), "Solar");

					solarDb.updateConsumerData(0, solarDb.getConsumerBudget(senderName)
							- ((SolarAgent) myAgent).getSolar().getSolarPrice() * solarDb.getConsumerEnReq(senderName),
							senderName);
					
				}
				new BaseAgent().sendMessageToAgentsByServiceType(this.myAgent, "BungalowAgent", "WakeUp", "Sveglia!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (this.myAgent instanceof AeolianAgent) {
			try {
				DbAeolianData aeolianDb = ((AeolianAgent) myAgent).getDbAeolian();
				String localAeolian = this.myAgent.getLocalName();
				String senderName = msg.getSender().getLocalName();
				
					if (aeolianDb.getMyKw(localAeolian) <= aeolianDb.getConsumerEnReq(senderName)) {

						// System.out.println(((SolarAgent) myAgent).getSolar().getCounterSolarKw());

						new BaseAgent().sendMessageToAgentsByServiceType(this.myAgent, msg.getSender(), "Finished",
								"Vendo a te " + (aeolianDb.getMyKw(localAeolian)
								+ " Kw."));

						aeolianDb.updateConsumerData(
								aeolianDb.getConsumerEnReq(senderName)
								- aeolianDb.getMyKw(localAeolian),
								aeolianDb.getConsumerBudget(senderName)
								- ((AeolianAgent) myAgent).getAeolian().getWindPrice()
								* aeolianDb.getMyKw(localAeolian),
								senderName);

		                aeolianDb.updateProviderData(0, ((AeolianAgent) myAgent).getAeolian().getWindPrice(), "Aeolian");

					} else {
						new BaseAgent().sendMessageToAgentsByServiceType(this.myAgent, msg.getSender(), "Finished",
								"Vendo a te " + aeolianDb.getConsumerEnReq(senderName) + " Kw.");

						aeolianDb.updateProviderData(aeolianDb.getMyKw(localAeolian)-aeolianDb.getConsumerEnReq(senderName), ((AeolianAgent) myAgent).getAeolian().getWindPrice(), "Aeolian");
						
						aeolianDb.updateConsumerData(0, aeolianDb.getConsumerBudget(senderName)
								- ((AeolianAgent) myAgent).getAeolian().getWindPrice() * aeolianDb.getConsumerEnReq(senderName),
								senderName);
						
					}
				new BaseAgent().sendMessageToAgentsByServiceType(this.myAgent, "BungalowAgent", "WakeUp", "Sveglia!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (this.myAgent instanceof BatteryAgent) {
			try {
				DbBatteryData batteryDb = ((BatteryAgent) myAgent).getDbBattery();
				String localBattery = this.myAgent.getLocalName();
				String senderName = msg.getSender().getLocalName();
				
				if (batteryDb.getMyKw(localBattery) <= batteryDb.getConsumerEnReq(senderName)) {

					new BaseAgent().sendMessageToAgentsByServiceType(this.myAgent, msg.getSender(), "Finished",
							"Vendo a te " + (batteryDb.getMyKw(localBattery)
							+ " Kw."));

					batteryDb.updateConsumerData(
							batteryDb.getConsumerEnReq(senderName)
							- batteryDb.getMyKw(localBattery),
							batteryDb.getConsumerBudget(senderName)
							- ((BatteryAgent) myAgent).getBattery().getBatteryPrice()
							* batteryDb.getMyKw(localBattery),
							senderName);

					batteryDb.updateProviderData(0, ((BatteryAgent) myAgent).getBattery().getBatteryPrice(), ((BatteryAgent) myAgent).getBattery().getBudget(), "Battery");

				} else {
					new BaseAgent().sendMessageToAgentsByServiceType(this.myAgent, msg.getSender(), "Finished",
							"Vendo a te " + batteryDb.getConsumerEnReq(senderName) + " Kw.");

					
					batteryDb.updateProviderData(batteryDb.getMyKw(localBattery)-batteryDb.getConsumerEnReq(senderName), ((BatteryAgent) myAgent).getBattery().getBatteryPrice(), ((BatteryAgent) myAgent).getBattery().getBudget(), "Battery");

					batteryDb.updateConsumerData(0, batteryDb.getConsumerBudget(senderName)
							- ((BatteryAgent) myAgent).getBattery().getBatteryPrice() * batteryDb.getConsumerEnReq(senderName),
							senderName);
					
				}
				new BaseAgent().sendMessageToAgentsByServiceType(this.myAgent, "BungalowAgent", "WakeUp", "Sveglia!");
				//inserire tale meccanismo anche nel Dso
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}