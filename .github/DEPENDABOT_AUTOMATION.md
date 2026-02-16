# Dependabot Automation Setup

This repository uses GitHub Actions to automatically approve and merge Dependabot pull requests for patch and minor version updates.

## Prerequisites

The automation workflow requires a Personal Access Token (PAT) with the following permissions:
- `pull_requests: write` - To approve pull requests
- `contents: write` - To enable auto-merge

### Important: CODEOWNERS Considerations

If you have CODEOWNERS configured and branch protection requires code owner reviews:

**Option 1 (Recommended): Exclude dependency files from CODEOWNERS**
- Add entries to `.github/CODEOWNERS` to exclude files like `pom.xml` from requiring code owner review
- This allows the workflow to approve and merge Dependabot PRs automatically

**Option 2: Use a PAT from a different user**
- If the PAT belongs to the same user as the CODEOWNER, GitHub prevents self-approval
- Create the PAT using a bot account or another admin user with repository access
- This allows the workflow to approve PRs that would otherwise require your review

**Option 3: Configure branch protection**
- In repository Settings → Branches → Branch protection rules
- Uncheck "Require review from Code Owners" OR
- Check "Allow specified actors to bypass required pull requests" and add Dependabot

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
