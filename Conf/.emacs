;; ------ ;;
;; SYSTEM ;;
;; ------ ;;

;; Extend path to local dump of emacs plugins and scripts
(add-to-list 'load-path "~/Workspace/bin/emacs/")
;; Contents:
;; - Package
;; - ESS
;; - Slime (inc. contrib)
;; - Nyan mode

;; Kill the damned menu bar
(menu-bar-mode 0)

;; Make scrolling sexily granular
(setq scroll-step 1)


;; -------- ;;
;; PACKAGES ;;
;; -------- ;;

;; Load Marmelade package manager
(load "package/lisp_package.el")
(require 'package)
(add-to-list 'package-archives
             '("marmalade" . "http://marmalade-repo.org/packages/"))
(package-initialize)

;; Stats package
(load "ess/site-lisp/ess-site.el")

;; Load and configure Slime (et contrib)
(load "slime/slime.el")
(load "slime/contrib/slime-banner.el")
(load "slime/contrib/slime-repl.el")
(require 'slime)
(eval-after-load "slime"
  '(progn
     (require 'slime-banner)
     (require 'slime-repl)
     (slime-banner-init)
     (slime-setup '(slime-repl))))


;; ----- ;;
;; BINDS ;;
;; ----- ;;

;; Binds for Slime interaction
(global-set-key (kbd "<f5>") 'clojure-jack-in)

;; Binds for entering lispy text
(global-set-key (kbd "M-a") "#")
(global-set-key (kbd "M-o") "/")
(global-set-key (kbd "M-e") "(")
(global-set-key (kbd "M-u") "[")
(global-set-key (kbd "M-i") "{")
(global-set-key (kbd "M-d") "}")
(global-set-key (kbd "M-h") "]")
(global-set-key (kbd "M-t") ")")
(global-set-key (kbd "M-n") "=")
(global-set-key (kbd "M-s") "?")
(global-set-key (kbd "M--") "^")

;; Binds for traversing lispy text
(global-set-key (kbd "C-h") 'forward-char)
(global-set-key (kbd "C-u") 'backward-char)
(global-set-key (kbd "C-t") 'forward-word)
(global-set-key (kbd "C-e") 'backward-word)
(global-set-key (kbd "C-n") 'forward-sexp)
(global-set-key (kbd "C-o") 'backward-sexp)
(global-set-key (kbd "C-s") 'forward-paragraph)
(global-set-key (kbd "C-a") 'backward-paragraph)

;; Binds for editing
(global-set-key (kbd "M-SPC") 'set-mark-command)
(global-set-key (kbd "M-m"  ) 'kill-region)
(global-set-key (kbd "M-w"  ) 'kill-ring-save)
(global-set-key (kbd "M-v"  ) 'yank)

;; Fix useful shortcuts which were overwritten
(global-set-key (kbd "C-M-h") 'help)
(global-set-key (kbd "C-M-u") 'universal-argument)

;; Custom help messages to spew above bindings
(global-set-key (kbd "C-M-t") (lambda () (interactive) (message "(meta) # / ( [ {   } ] ) = ? ^")))
(global-set-key (kbd "C-M-n") (lambda () (interactive) (message "(ctrl) para: a/s  sexp: o/n  word: e/t  char: u/h")))