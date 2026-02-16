# Dependabot Automation Setup

This repository uses GitHub Actions to automatically approve and merge Dependabot pull requests for patch and minor version updates.

## Prerequisites

The automation workflow requires a Personal Access Token (PAT) with the following permissions:
- `pull_requests: write` - To approve pull requests
- `contents: write` - To enable auto-merge

### Important: CODEOWNERS and Self-Approval

**If you are set as the code owner for files Dependabot updates (like `pom.xml`), the current workflow WILL NOT bypass your review requirement.**

This is because:
1. GitHub prevents self-approval when you're listed as a CODEOWNER
2. Even if the workflow approves the PR using your PAT, it counts as you approving your own PR
3. Branch protection rules requiring code owner reviews will still block the merge

### Solutions to Bypass CODEOWNERS Review

Choose one of these options to allow automatic merging:

**Option 1 (Recommended): Use a PAT from a different user**
- Create the PAT using a bot account or another admin user with repository access
- This allows the workflow to provide a legitimate external approval
- The approval will satisfy CODEOWNERS requirements since it's not self-approval

**Option 2: Configure branch protection to allow Dependabot bypass**
- Go to repository Settings → Branches → Branch protection rules for `main`
- Under "Require a pull request before merging":
  - Check "Allow specified actors to bypass required pull requests"
  - Add `dependabot` to the bypass list
- This allows Dependabot PRs to merge without requiring reviews

**Option 3: Disable CODEOWNERS requirement for branch protection**
- Go to repository Settings → Branches → Branch protection rules for `main`
- Uncheck "Require review from Code Owners"
- Note: This removes CODEOWNERS enforcement for all PRs, not just Dependabot

**Current Setup**: If you use your own PAT, Dependabot PRs will be approved but will still wait for you to manually merge them after CI passes, since the approval counts as self-approval.

## Setup Instructions

1. **Create a Fine-grained Personal Access Token:**
   - Go to GitHub Settings → Developer settings → Personal access tokens → Fine-grained tokens
   - Click "Generate new token"
   - Set the token name (e.g., "Dependabot Automation")
   - Select this repository
   - Set the following permissions:
     - Repository permissions → Pull requests: Read and write
     - Repository permissions → Contents: Read and write
   - Generate the token and copy it

2. **Add the token as a repository secret:**
   - Go to the repository Settings → Secrets and variables → Actions
   - Click "New repository secret"
   - Name: `PAT_TOKEN`
   - Value: Paste the token you generated
   - Click "Add secret"

3. **Ensure branch protection is configured:**
   - Go to Settings → Branches → Branch protection rules for `main`
   - Enable "Allow auto-merge"
   - Configure required status checks (e.g., the `build` workflow should pass)

## How It Works

The workflow (`.github/workflows/dependabot-automation.yaml`) automatically:
1. Detects when a PR is opened by Dependabot
2. Fetches metadata about the dependency update
3. Approves the PR using the PAT token
4. Enables auto-merge for patch and minor version updates
5. The PR will be merged automatically once all required checks pass

## Supported Update Types

The workflow will auto-merge:
- **Patch updates** (e.g., 1.0.0 → 1.0.1)
- **Minor updates** (e.g., 1.0.0 → 1.1.0)

Major version updates require manual review and approval.
