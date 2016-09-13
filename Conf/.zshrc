# ZedShell options
autoload -U compinit
compinit -C
# Allow tab completion in the middle of a word
setopt COMPLETE_IN_WORD
setopt correctall
# Group matches by their description
zstyle ':completion:*:descriptions' format '%U%B%d%b%u'
# Match completions without case sensitivity
zstyle ':completion:*' matcher-list 'm:{a-zA-Z}={A-Za-z}' 'r:|[._-]=* r:|=*' 'l:|=* r:|=*'

# Stop warning me about rspec!
alias bundle='nocorrect bundle'

# Load prompt colors
autoload -U colors
colors

# Customized pretty-prompt
export PROMPT="%{$fg[blue]%}[%{$reset_color%}%(!.%{$fg[red]%}root%{$reset_color%}.%n)%{$fg[blue]%}]%{$reset_color%} > "
export RPROMPT="%{$fg[yellow]%}%~%{$reset_color%}"

# Record shell command history
export HISTSIZE=10000
export HISTFILE="$HOME/.zsh_history"
export SAVEHIST=$HISTSIZE
setopt hist_ignore_all_dups
# Commands with preceeding space will not be logged
setopt hist_ignore_space

# When piping X force emacs to open in the terminal
alias emacs="emacs -nw"

# General shorthand and mis-typed stuff
alias l="ls"
alias g="grep"

# Brew pernsonal token
export HOMEBREW_GITHUB_API_TOKEN=REDACTED

# RbEnv initialization
export GEM_PATH=/Users/amcnamara/.rbenv/versions/2.2.3/lib/ruby/gems/2.2.0
eval "$(rbenv init -)"

# Tmuxinator configs
export EDITOR="emacs"
export SHELL="zsh"

# SDEdit on Path
export PATH=$PATH:~/SDEdit

# ChefDK on Path
export PATH=/usr/local/bin:$PATH

# Brew binaries on Path
export PATH="/usr/local/sbin:$PATH"

# Terraform binaries on Path
export PATH="/usr/local/terraform:$PATH"

# Start NVM
export NVM_DIR=~/.nvm
source $(brew --prefix nvm)/nvm.sh

# For vagrant helper scripts to find outreach sources
export LOCAL_SRC_DIR=~/Workspace

export CLASSPATH=~/.m2/
