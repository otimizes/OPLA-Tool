# OPLA-Tool Usage
The OPLA-Tool is used to automate the MOA4PLA approach. This documentation was created to assist you in the usage of it.
### Step 1: Login
Firstly, you must to login in the tool. If you dont have an account, it will create at first login time.
 
![Step 1](https://raw.githubusercontent.com/SBSE-UEM/OPLA-Tool/willian/docs/usage/step-1.png)
### Step 2: Logged User
After your login, this page will appear. 

![Step 2](https://raw.githubusercontent.com/SBSE-UEM/OPLA-Tool/willian/docs/usage/step-2.png)
### Step 3: Experiment configurations

In this version, you can configure several parameters. They are introduced as follow.
#### Step 3.0: Objective functions
The first one is the objective functions. In this example, it was selected three of them. 

![Step 3-0](https://raw.githubusercontent.com/SBSE-UEM/OPLA-Tool/willian/docs/usage/step-3-0.png)
#### Step 3.1: Select the PLA
The PLA project can be selected is this card. There are two tools (builders) you can use to manipulate the UML Diagrams.
The papyrus is a old version and does not supports to load SPL elements. We advice you to use the SMarty tool.

![Step 3-1](https://raw.githubusercontent.com/SBSE-UEM/OPLA-Tool/willian/docs/usage/step-3-1.png)
#### Step 3.2: Optimization algorithm settings
In this card, you will able to select an optimization algorithm, as well as configure the number of runs and
max evaluations. If you are using the PAES, it will be necessary to set the archive size. Other algorithms require
the population size parameter.

![Step 3-2](https://raw.githubusercontent.com/SBSE-UEM/OPLA-Tool/willian/docs/usage/step-3-2.png)

If you desire to interact during the optimization process, you can enable it in this card.
#### Step 3.3: User's Interaction
![Step 3-3](https://raw.githubusercontent.com/SBSE-UEM/OPLA-Tool/willian/docs/usage/step-3-3.png)
#### Step 3.4: Mutation and crossover probability
If it will be used the mutation and crossover operators, it is necessary to select the options in this card.

![Step 3-4](https://raw.githubusercontent.com/SBSE-UEM/OPLA-Tool/willian/docs/usage/step-3-4.png)
#### Step 3.5: Mutation operators
These are the mutation operators present in the tool, generally by default, all are selected. 

![Step 3-5](https://raw.githubusercontent.com/SBSE-UEM/OPLA-Tool/willian/docs/usage/step-3-5.png)
#### Step 3.6: Design patterns
The design patterns are applied in the PLA as mutation operators. However, you can find them here, in this tab.

### Step 4: Running the experiment
In the right inferior button, it is possible to run the optimization process. When the process initialize,
this button is disabled.

![Step 4](https://raw.githubusercontent.com/SBSE-UEM/OPLA-Tool/willian/docs/usage/step-4.png)
### Step 5: View logs
In this tab, you are able to see the logs that show the optimization process status.

![Step 5](https://raw.githubusercontent.com/SBSE-UEM/OPLA-Tool/willian/docs/usage/step-5.png)
### Step 6: Download PLA
When the optimization process end, the right inferior button change, enabling of the optimized solutions.

![Step 6](https://raw.githubusercontent.com/SBSE-UEM/OPLA-Tool/willian/docs/usage/step-6.png)
### Step 7: View experiments
This tab contains all executions from experiments.

![Step 7](https://raw.githubusercontent.com/SBSE-UEM/OPLA-Tool/willian/docs/usage/step-7.png)
### Step 8: Analyses results
Finally, you can analyse the results. Just select the desired experiments, and the graphics will be shown.

![Step 8](https://raw.githubusercontent.com/SBSE-UEM/OPLA-Tool/willian/docs/usage/step-8.png)
