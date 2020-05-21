# Contributing
This document will assist you in the process of contributing to the project.
### Step 1: Fork
First, click on fork to create a copy of the project in your account
 
![Step 1](https://raw.githubusercontent.com/otimizes/OPLA-Tool/master/docs/contributing/step-1.png)
### Step 2: After fork
After creating the fork, a screen will appear like this, click refresh.

![Step 2](https://raw.githubusercontent.com/otimizes/OPLA-Tool/master/docs/contributing/step-2.png)
#### Step 2.1: Clone the project by url
Please note that a new project (link) will be created in your account. Click on clone or download and copy the project link. 

![Step 3](https://raw.githubusercontent.com/otimizes/OPLA-Tool/master/docs/contributing/step-3.png)
### Step 4: Commit changes
To clone run the command in the terminal: git clone copied_link

After making the necessary changes, it is time to commit those changes.

To do this, run in the project's root directory:
```shell script
git add.
git commit -m "Some Comments"
git push origin master
```
Open the project link on github, and if it is open, refresh the page. 

Thus, you will see your changes committed on github. 

Attention, only perform the next step if you have completed all the changes and performed the necessary tests. Otherwise, be aware that your pull request may fail.


### Step 5: New pull request
After entering the project page in your account, click on New pull request.

![Step 5](https://raw.githubusercontent.com/otimizes/OPLA-Tool/master/docs/contributing/step-5.png)
### Step 6: Create pull request
Note that a screen will open with your changes. Click Create pull request.

![Step 6](https://raw.githubusercontent.com/otimizes/OPLA-Tool/master/docs/contributing/step-6.png)
### Step 7: Send pull request
You are now opening a pull request, which is nothing more than a request to include your changes in the official version of the project. Write a comment and click Create Pull Request.

![Step 6](https://raw.githubusercontent.com/otimizes/OPLA-Tool/master/docs/contributing/step-7.png)

## Very important notes, read carefully
When submitting your pull request, you will be sending your changes to the official version of the project, so keep in mind that your version must have the following prerequisites:
- The code must be clean, and it cannot be duplicated;
- You will be responsible for your code. As much as there is a pull request reviewer evaluating your code, it is not the responsibility of the reviewer to test the entire tool for implementation failures. The same, will review your code and tests, seeking the proper functioning of the tool, but any problem that occurs related to your code, is linked to your github account.
- Tests created in Junit are essential to validate the correctness of your code, if someone in the future changes something that impacts their implementation, the tests will block the execution of the project;
- The use of the Intellij ide is recommended, as it offers several features in relation to Netbeans. It is possible to use the community version or the student version;
- Do not change the changelog and README.md without first informing the reviewers;
- When creating the pull request, add comments that reflect exactly what was done in the tool. The same will be used by the reviewer when updating the changelog.

Ready!!! See how is it easy? Thus we work in an organized manner and contribute to the smooth running of the project. The community thanks ...