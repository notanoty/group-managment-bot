import logging
import os

from dotenv import load_dotenv
from telegram import ForceReply, Update

from telegram.ext import Application, CommandHandler, ContextTypes, MessageHandler, filters

import handlers
from handlers.echo import echo
from handlers.help import help_command

# Enable logging

logging.basicConfig(

    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s", level=logging.INFO

)

# set higher logging level for httpx to avoid all GET and POST requests being logged

logging.getLogger("httpx").setLevel(logging.WARNING)


logger = logging.getLogger(__name__)





async def start(update: Update, context: ContextTypes.DEFAULT_TYPE) -> None:
    user = update.effective_user

    await update.message.reply_html(

        rf"Hi {user.mention_html()}!",

        reply_markup=ForceReply(selective=True),

    )




def main() -> None:

    """Start the bot."""
    load_dotenv()
    # Create the Application and pass it your bot's token.

    application = Application.builder().token(os.getenv('TOKEN')).build()


    # on different commands - answer in Telegram

    application.add_handler(CommandHandler("start", start))

    application.add_handler(CommandHandler("help", help_command))


    # on non command i.e message - echo the message on Telegram

    application.add_handler(MessageHandler(filters.TEXT & ~filters.COMMAND, echo))


    # Run the bot until the user presses Ctrl-C

    application.run_polling(allowed_updates=Update.ALL_TYPES)



if __name__ == "__main__":

    main()