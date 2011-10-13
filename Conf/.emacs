;; ------ ;;
;; SYSTEM ;;
;; ------ ;;

;; Set default interactive lisp to Clojure
;(setq inferior-lips-program "/opt/local/bin/clj")

;; Extend path to local dump of emacs plugins and scripts
(add-to-list 'load-path "~/Workspace/bin/emacs/")
;; Contents:
;; - Package
;; - ESS (not currently active in this setup, see: 'Stats plugin' below)
;; - Slime with contrib

;; Kill the menu bar
(menu-bar-mode 0)

;; Make scrolling granular
(setq scroll-step 1)

;; Don't create backups on edit
(setq make-backup-files nil)

;; Kill scratch header
(setq initial-scratch-message nil)


;; ---------- ;;
;; EXTENSIONS ;;
;; ---------- ;;

;; Load Multi-term
;; Modify buffer call behaviour such that:
;;  - If term buffer exists in bg, bring it to fg on call
;;  - If no term buffer exists in bg, create new one
;;  - If term buffer has focus, create another term buffer on call
(load "multi-term/multi-term.el")
(require 'multi-term)
(setq multi-term-program "/bin/bash")

(defun last-term-buffer (l)
  "Return most recently used term buffer."
  (when l
    (if (eq 'term-mode (with-current-buffer (car l) major-mode))
	(car l) (last-term-buffer (cdr l)))))

(defun get-term ()
  "Switch to the term buffer last used, or create a new one if
    none exists, or if the current buffer is already a term."
  (interactive)
  (let ((b (last-term-buffer (buffer-list))))
    (if (or (not b) (eq 'term-mode major-mode))
	(multi-term)
      (switch-to-buffer b))))

;; Load Marmelade package manager
(load "package/lisp_package.el")
(require 'package)
(add-to-list 'package-archives
             '("marmalade" . "http://marmalade-repo.org/packages/"))
(package-initialize)

;; Stats plugin
;(load "ess/site-lisp/ess-site.el")

(load "word-count-mode/word-count.el")
(require 'word-count)

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

;; Binds for scripts and plugins
(global-set-key (kbd "<f5>") 'clojure-jack-in)
(global-set-key (kbd "<f6>") 'get-term)

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


;; ------- ;;
;; STARTUP ;;
;; ------- ;;

;; Load dired Workspace on startup
(setq initial-buffer-choice "~/Workspace")

;; Kill Workspace buffer if file loaded from shell
(defun no-initial-buffer ()
  (setq initial-buffer-choice nil))
(add-hook 'find-file-hook 'no-initial-buffer)


;; --------------- ;;
;; Autogen Configs ;;
;; --------------- ;;

(custom-set-variables
  ;; custom-set-variables was added by Custom.
  ;; If you edit it by hand, you could mess it up, so be careful.
  ;; Your init file should contain only one such instance.
  ;; If there is more than one, they won't work right.
 '(clojure-defun-indents (quote (macro-do anon-macro macrolet lazy-loop with-adjustments assuming form-to POST GET delegating-deftype flash-error flash-msg))))
(custom-set-faces
  ;; custom-set-faces was added by Custom.
  ;; If you edit it by hand, you could mess it up, so be careful.
  ;; Your init file should contain only one such instance.
  ;; If there is more than one, they won't work right.
 )
