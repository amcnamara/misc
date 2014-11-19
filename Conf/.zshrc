autoload -U compinit
compinit

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

# Load prompt colors                                                                                                                                                                                                                                                                                                          
autoload -U colors
colors

# Customized pretty-prompt                                                                                                                                                                                                                                                                                                    
export PS1="%{$fg[blue]%}[%{$reset_color%}alex%{$fg[blue]%}]%{$reset_color%} > "

# Record shell command history                                                                                                                                                                                                                                                                                                
export HISTSIZE=10000
export HISTFILE="$HOME/.zsh_history"
export SAVEHIST=$HISTSIZE
setopt hist_ignore_all_dups
# Commands with preceeding space will not be logged                                                                                                                                                                                                                                                                           
setopt hist_ignore_space

# When piping X force emacs to open in the terminal                                                                                                                                                                                                                                                                           
alias emacs="emacs -nw"
