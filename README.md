# README for the Application GitRepoExplorer
Recruitment task for Atipera.

## GitRepoExplorer
GitRepoExplorer is an application that allows you to search for user repositories on GitHub.

### Required Environment Variables
Before running this application, make sure the following environment variables are defined:
* GITHUB_TOKEN - Fine-grained personal access GitHub token. To generate your Github personal access token -
Visit the [Personal Access Tokens](https://github.com/settings/tokens?type=beta) section on Github

### Using the application
After the application is running, the application will be accessible at: http://localhost:8080/

### Endpoints
* `GET /repository/{username}` - Endpoint allows you to retrieve information about the repositories of a user with a given username, which are not forks.

#### Path Variables
* `{username}` - The name of the user for whom we want to retrieve data.

#### Response
When the query is correct, the server returns a response with code 200 and data in the form of JSON containing a list of repositories.

* `ownerLogin` - The name of the repository owner
* `name` - List of branches in the repository. Each branch is represented as an object with fields:
* `branches` -
    * `name` - Branch name
    * `lastCommitSha` - SHA of the last commit in a given branch.

e.g.: 

```json
  {
    "repositories": [
      {
        "ownerLogin": "OpusMagnus5",
        "name": "GitRepoExplorer",
        "branches": [
          {
            "name": "master",
            "lastCommitSha": "e80b708f448b9feeea85a0bdb20cfef6b12ede26"
          }
        ]
      }
    ]
  }
```

If the user does not exist, the API returns code 404 and an appropriate message e.g.:

```json
{
    "status": 404,
    "message": "No user found with the provided username in the external data source."
}
```

If other errors occur, the application returns a response in the identical format.


