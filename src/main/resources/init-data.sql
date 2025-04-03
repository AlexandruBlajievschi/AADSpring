-- ============================================================
-- Categories
-- ============================================================

INSERT INTO categories (name, description)
VALUES
    ('Risks', 'Articles that discuss the dangers in crypto, from scams and security pitfalls to market volatility.'),
    ('Wallets', 'Comprehensive guides on storing, managing, and safeguarding your cryptocurrency.'),
    ('Basics', 'Detailed explanations of fundamental crypto concepts for beginners.'),
    ('Markets', 'Analyses and strategies covering crypto market trends and trading techniques.')
    ON CONFLICT (name) DO NOTHING;

-- ============================================================
-- Wallets Articles
-- ============================================================

-- (1) List of DEX Platforms (content left empty as requested)
INSERT INTO articles (content, title, category_id)
VALUES (
           '',
           'List of DEX Platforms',
           (SELECT id FROM categories WHERE name = 'Wallets')
       )
    ON CONFLICT (title) DO NOTHING;

-- (2) List of CEX Platforms
INSERT INTO articles (content, title, category_id)
VALUES (
           'Centralized exchanges (CEX) are digital platforms that facilitate buying, selling, and trading cryptocurrencies by managing user accounts and holding custody of funds. Notable examples include Binance, Bybit, Coinbase Exchange, Upbit, OKX, Bitget, MEXC, Gate.io, KuCoin, and Crypto.com Exchange. These platforms typically offer high liquidity, user-friendly interfaces, and additional services such as fiat on-ramps and customer support.',
           'List of CEX Platforms',
           (SELECT id FROM categories WHERE name = 'Wallets')
       )
    ON CONFLICT (title) DO NOTHING;

-- (3) What Is a Crypto Wallet?
INSERT INTO articles (content, title, category_id)
VALUES (
           'A crypto wallet is a secure software or hardware solution that stores your private keys—the secret codes that allow you to access and manage your digital assets on the blockchain. This tool enables you to sign transactions and interact directly with the network while providing various layers of security such as encryption and backup options to protect your funds from unauthorized access.',
           'What Is a Crypto Wallet?',
           (SELECT id FROM categories WHERE name = 'Wallets')
       )
    ON CONFLICT (title) DO NOTHING;

-- (4) How to Set Up a Crypto Wallet (Step-by-Step)
INSERT INTO articles (content, title, category_id)
VALUES (
           'To set up a crypto wallet, start by downloading a trusted wallet application like MetaMask or Trust Wallet. Upon installation, create a new wallet account and write down the generated seed phrase on paper; this phrase is essential for recovery. Next, secure your wallet with a strong password and enable any available security features, such as two-factor authentication. Finally, test the wallet by sending a small transaction to ensure everything is configured correctly before transferring larger amounts.',
           'How to Set Up a Crypto Wallet (Step-by-Step)',
           (SELECT id FROM categories WHERE name = 'Wallets')
       )
    ON CONFLICT (title) DO NOTHING;

-- (5) Understanding Seed Phrases and Why They’re Important
INSERT INTO articles (content, title, category_id)
VALUES (
           'Seed phrases are a series of words generated during wallet creation that serve as a backup for your private keys. They allow you to restore your wallet in the event of device loss or failure. Since anyone with access to your seed phrase can control your funds, it is crucial to write it down immediately and store it in a secure, offline location, such as a safe or a safety deposit box.',
           'Understanding Seed Phrases and Why They’re Important',
           (SELECT id FROM categories WHERE name = 'Wallets')
       )
    ON CONFLICT (title) DO NOTHING;

-- (6) Sending and Receiving Crypto Using a Wallet
INSERT INTO articles (content, title, category_id)
VALUES (
           'When sending cryptocurrency, enter the recipient’s wallet address carefully, specify the amount, and confirm the transaction after reviewing any associated network fees. Conversely, to receive crypto, provide your wallet address to the sender. It is important to verify addresses using copy-paste or QR code scanning to avoid errors. This process ensures secure and accurate transfer of digital assets between parties.',
           'Sending and Receiving Crypto Using a Wallet',
           (SELECT id FROM categories WHERE name = 'Wallets')
       )
    ON CONFLICT (title) DO NOTHING;

-- ============================================================
-- Risks Articles
-- ============================================================

-- (1) Common Crypto Scams and How to Spot Them
INSERT INTO articles (content, title, category_id)
VALUES (
           'Crypto scams are deceptive schemes that trick users into giving up funds or personal information. Common scams include phishing emails, fake investment opportunities, and fraudulent giveaways that mimic reputable brands. By checking URLs carefully, verifying the authenticity of communications, and avoiding too-good-to-be-true offers, users can protect themselves from these malicious practices.',
           'Common Crypto Scams and How to Spot Them',
           (SELECT id FROM categories WHERE name = 'Risks')
       )
    ON CONFLICT (title) DO NOTHING;

-- (2) What Happens If You Lose Your Wallet Access?
INSERT INTO articles (content, title, category_id)
VALUES (
           'Losing access to your crypto wallet—whether through a forgotten password, lost device, or misplacement of your seed phrase—can lead to irreversible loss of funds. This article explains the importance of secure backups and outlines recovery strategies, such as using hardware wallets or multi-signature setups, to minimize the risk of permanent loss.',
           'What Happens If You Lose Your Wallet Access?',
           (SELECT id FROM categories WHERE name = 'Risks')
       )
    ON CONFLICT (title) DO NOTHING;

-- (3) How to Avoid Phishing & Fake Websites
INSERT INTO articles (content, title, category_id)
VALUES (
           'Phishing attacks and fake websites are designed to steal your login credentials and private data. The best defenses include manually typing URLs, using trusted bookmarks, and verifying website security certificates (HTTPS). Additionally, installing reputable browser extensions and staying informed about common phishing tactics can significantly reduce the risk of falling victim to these schemes.',
           'How to Avoid Phishing & Fake Websites',
           (SELECT id FROM categories WHERE name = 'Risks')
       )
    ON CONFLICT (title) DO NOTHING;

-- (4) The Risk of Leaving Crypto on Exchanges
INSERT INTO articles (content, title, category_id)
VALUES (
           'Leaving your cryptocurrency on an exchange may seem convenient, but it exposes you to risks such as hacking, fraud, and loss of custody. Exchanges hold your funds in a centralized manner, which can be vulnerable to security breaches. Transferring assets to a personal, non-custodial wallet gives you full control over your keys and reduces these risks considerably.',
           'The Risk of Leaving Crypto on Exchanges',
           (SELECT id FROM categories WHERE name = 'Risks')
       )
    ON CONFLICT (title) DO NOTHING;

-- (5) Emotional Investing: FOMO and FUD Explained
INSERT INTO articles (content, title, category_id)
VALUES (
           'Emotional factors like FOMO (fear of missing out) and FUD (fear, uncertainty, and doubt) often drive impulsive decisions in the crypto market. These psychological forces can lead to buying high in a frenzy or selling low in a panic. Learning to recognize these emotions and relying on data-driven analysis instead of impulse can help maintain a disciplined, long-term investment strategy.',
           'Emotional Investing: FOMO and FUD Explained',
           (SELECT id FROM categories WHERE name = 'Risks')
       )
    ON CONFLICT (title) DO NOTHING;

-- (6) Volatility and How to Handle It
INSERT INTO articles (content, title, category_id)
VALUES (
           'The cryptocurrency market is characterized by high volatility, with dramatic price swings influenced by market sentiment, news, and global events. Effective strategies to handle volatility include diversifying your investments, setting stop-loss orders, and maintaining a long-term perspective. Understanding these fluctuations can help you manage risk and avoid reactive decisions during market turbulence.',
           'Volatility and How to Handle It',
           (SELECT id FROM categories WHERE name = 'Risks')
       )
    ON CONFLICT (title) DO NOTHING;

-- ============================================================
-- Basics Articles
-- ============================================================

-- (1) Introduction to Cryptocurrency
INSERT INTO articles (content, title, category_id)
VALUES (
           'Cryptocurrency is a form of digital currency that operates on decentralized networks using cryptography for secure transactions. Unlike traditional money, cryptocurrencies are not controlled by central banks and offer the promise of transparency and lower transaction fees. This technology has paved the way for innovative financial systems and applications.',
           'Introduction to Cryptocurrency',
           (SELECT id FROM categories WHERE name = 'Basics')
       )
    ON CONFLICT (title) DO NOTHING;

-- (2) Understanding Blockchain Technology
INSERT INTO articles (content, title, category_id)
VALUES (
           'Blockchain is a distributed ledger that records transactions across multiple computers in an immutable and transparent way. Each block in the chain contains a set of transactions, and once data is added it cannot be altered. This technology underpins all cryptocurrencies and is widely recognized for its potential to revolutionize industries beyond finance.',
           'Understanding Blockchain Technology',
           (SELECT id FROM categories WHERE name = 'Basics')
       )
    ON CONFLICT (title) DO NOTHING;

-- (3) How to Buy Your First Cryptocurrency
INSERT INTO articles (content, title, category_id)
VALUES (
           'To buy your first cryptocurrency, begin by choosing a reliable exchange. Create an account and complete the necessary verification steps. Deposit funds using your preferred payment method, then place a buy order for a cryptocurrency such as Bitcoin or Ethereum. Finally, transfer your assets to a secure wallet to ensure that you maintain full control over your funds.',
           'How to Buy Your First Cryptocurrency',
           (SELECT id FROM categories WHERE name = 'Basics')
       )
    ON CONFLICT (title) DO NOTHING;

-- (4) The Role of Decentralization in Crypto
INSERT INTO articles (content, title, category_id)
VALUES (
           'Decentralization is a fundamental aspect of cryptocurrencies that eliminates the need for intermediaries such as banks. In a decentralized network, transactions are validated by multiple nodes, which enhances security and transparency. This structure empowers users, reduces single points of failure, and fosters an open financial ecosystem.',
           'The Role of Decentralization in Crypto',
           (SELECT id FROM categories WHERE name = 'Basics')
       )
    ON CONFLICT (title) DO NOTHING;

-- (5) Crypto Terminology for Beginners
INSERT INTO articles (content, title, category_id)
VALUES (
           'Navigating the crypto space requires familiarity with key terms: "blockchain" refers to the distributed ledger technology; "wallet" is the secure tool to manage private keys; "mining" is the process of validating transactions; "token" is a digital asset; and "smart contract" is self-executing code that runs on the blockchain. Understanding these terms provides a solid foundation for further exploration.',
           'Crypto Terminology for Beginners',
           (SELECT id FROM categories WHERE name = 'Basics')
       )
    ON CONFLICT (title) DO NOTHING;

-- (6) Risks and Rewards of Crypto Investing
INSERT INTO articles (content, title, category_id)
VALUES (
           'Crypto investing offers the potential for high returns but is accompanied by significant risks. Volatility, regulatory uncertainty, and security challenges are key factors to consider. Successful investors balance these risks by diversifying their portfolios, conducting thorough research, and setting realistic expectations. This approach can help you capitalize on opportunities while mitigating potential losses.',
           'Risks and Rewards of Crypto Investing',
           (SELECT id FROM categories WHERE name = 'Basics')
       )
    ON CONFLICT (title) DO NOTHING;

-- ============================================================
-- Markets Articles
-- ============================================================

-- (1) Crypto Market Trends
INSERT INTO articles (content, title, category_id)
VALUES (
           'The crypto market is evolving rapidly as technological innovations, regulatory developments, and investor sentiment shift continuously. Recent trends include growing institutional adoption, the expansion of decentralized finance (DeFi), and increased market liquidity. Analyzing these trends provides insights into market cycles and helps investors identify potential opportunities.',
           'Crypto Market Trends',
           (SELECT id FROM categories WHERE name = 'Markets')
       )
    ON CONFLICT (title) DO NOTHING;

-- (2) How to Analyze Crypto Charts
INSERT INTO articles (content, title, category_id)
VALUES (
           'Analyzing crypto charts involves studying historical price data, volumes, and technical indicators to forecast future movements. Key concepts include identifying trends, recognizing support and resistance levels, and using tools such as moving averages and RSI. Mastering chart analysis equips traders with the ability to make informed, data-driven decisions.',
           'How to Analyze Crypto Charts',
           (SELECT id FROM categories WHERE name = 'Markets')
       )
    ON CONFLICT (title) DO NOTHING;

-- (3) Understanding Market Capitalization in Crypto
INSERT INTO articles (content, title, category_id)
VALUES (
           'Market capitalization is calculated by multiplying a cryptocurrency’s current price by its circulating supply. This metric provides an indication of the overall size and market value of a digital asset. A high market cap often suggests a more established and stable asset, while lower caps may indicate higher growth potential coupled with greater risk.',
           'Understanding Market Capitalization in Crypto',
           (SELECT id FROM categories WHERE name = 'Markets')
       )
    ON CONFLICT (title) DO NOTHING;

-- (4) The Impact of Global Events on Crypto Markets
INSERT INTO articles (content, title, category_id)
VALUES (
           'Global events such as economic downturns, regulatory changes, and geopolitical conflicts can trigger significant shifts in cryptocurrency markets. These events influence investor sentiment and can lead to rapid price fluctuations. Understanding how international news and macroeconomic factors affect the market helps investors better prepare for sudden changes.',
           'The Impact of Global Events on Crypto Markets',
           (SELECT id FROM categories WHERE name = 'Markets')
       )
    ON CONFLICT (title) DO NOTHING;

-- (5) Basics of Crypto Trading Strategies
INSERT INTO articles (content, title, category_id)
VALUES (
           'Crypto trading strategies vary from day trading and swing trading to long-term holding. Effective strategies involve technical analysis, risk management through stop-loss orders, and diversification across multiple assets. By combining these techniques, traders can capitalize on short-term price movements while maintaining a long-term investment perspective.',
           'Basics of Crypto Trading Strategies',
           (SELECT id FROM categories WHERE name = 'Markets')
       )
    ON CONFLICT (title) DO NOTHING;

-- (6) Future Outlook for Cryptocurrencies
INSERT INTO articles (content, title, category_id)
VALUES (
           'The future of cryptocurrencies is shaped by continuous technological advancements, regulatory evolution, and growing market adoption. This article examines potential developments such as the integration of decentralized finance, advancements in blockchain scalability, and the possible impacts of government policies. Such trends will influence both the opportunities and risks facing investors in the coming years.',
           'Future Outlook for Cryptocurrencies',
           (SELECT id FROM categories WHERE name = 'Markets')
       )
    ON CONFLICT (title) DO NOTHING;

INSERT INTO terms (term, meaning) VALUES ('Address', 'A special code made of numbers and letters that people use to send and receive crypto, like a digital home address.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Airdrop', 'Free crypto given to people for joining or helping a project, like getting a free gift bag at an event.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Altcoin', 'Any coin that isn’t Bitcoin. Think of them like different brands of soda, with Bitcoin as the original.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Blockchain', 'A digital notebook that records all crypto transactions and can’t be changed.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Bitcoin', 'The very first and most famous cryptocurrency, kind of like the ''gold'' of crypto.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Bear Market', 'When prices go down for a long time, like a rainy season for investors.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Coin', 'A type of cryptocurrency that lives on its own network, like how the Euro is used in the Eurozone.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Cold Wallet', 'A way to keep your crypto safe offline, like locking your money in a real-world safe.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Crypto Wallet', 'An app or device to hold your crypto, like a digital piggy bank.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('DeFi', 'Using crypto tools for loans or trading without banks, like doing banking without bankers.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('DAO', 'A group of people who run a project together using rules written in code instead of managers.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('DApp', 'An app built on blockchain, kind of like a regular app but no one owns it.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Ethereum', 'A popular crypto platform that lets people build apps and smart contracts.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Exchange', 'A place where you can trade your crypto for other coins or real money.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Encryption', 'A way to hide your data so only you or someone you trust can see it.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Fiat', 'Normal money like dollars or euros, not digital coins.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('FOMO', 'Feeling like you’ll miss out if you don’t buy crypto right now, like when all your friends go to a party without you.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Fork', 'When a crypto splits into two versions, like when a video game gets a new update.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Gas Fees', 'Small payments you make to get your crypto transaction processed, like paying for shipping.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Genesis Block', 'The very first block in a blockchain, like the first page of a storybook.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Gwei', 'Tiny pieces of Ethereum used to pay gas fees, like cents in a dollar.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Halving', 'An event that cuts mining rewards in half, making the coin harder to earn.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Hash Rate', 'How fast computers are solving problems to keep the blockchain running.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('HODL', 'A funny way to say ''hold'' — it means keeping your crypto no matter what.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Interest Rate', 'It’s how much extra you earn or pay when you lend or borrow money. In crypto, it’s like getting a small reward every day when you lend your coins.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Isolated Margin', 'It’s a way to keep your risky trade in its own box. If things go wrong, you only lose the money in that one box.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('ICO', 'It means “Initial Coin Offering.” It’s like when a new game is coming out and they sell early access packs.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('KYC', 'A process where you prove who you are before using a crypto exchange, like showing your ID.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Key Pair', 'Two codes: one you keep secret (private key), one you can share (public key), like a locked mailbox.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Kilobyte', 'A small chunk of data used in computers, kind of like a teaspoon of digital info.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Ledger', 'A record book that stores every crypto move ever made.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Liquidity', 'How easy it is to turn your crypto into cash or other coins.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Lightning Network', 'A faster way to send Bitcoin with fewer fees, like a shortcut road.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Mining', 'Using computers to earn crypto by solving puzzles.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Market Cap', 'The total value of all coins of one crypto added together.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Metamask', 'A popular digital wallet you can use in your web browser.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Node', 'A computer that helps run and update the blockchain.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('NFT', 'A one-of-a-kind digital item, like a special trading card online.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Nonce', 'A one-time number used in crypto math to keep things secure.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Oracle', 'A tool that brings real-world info into smart contracts, like a weather app for blockchains.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('On-chain', 'Stuff that happens directly on the blockchain.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Order Book', 'A list of who wants to buy or sell crypto at different prices.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Private Key', 'Your secret password to control your crypto wallet.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Public Key', 'A code you give people so they can send you crypto.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Proof of Work', 'A way to show that a computer worked hard to add a new block to the chain.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('QR Code', 'A scannable picture that holds info like your wallet address.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Quorum', 'The minimum number of people or computers needed to agree on a decision.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('QuickSwap', 'A crypto trading app built on the Polygon blockchain.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Rug Pull', 'When developers disappear with everyone’s money after launching a project.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('ROI', 'Return on Investment — how much you made or lost after investing.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Recovery Phrase', 'A secret list of words to help you get your crypto back if you lose access.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Smart Contract', 'A digital deal that runs automatically when conditions are met.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Staking', 'Locking up crypto to help a network and earn rewards.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Stablecoin', 'A crypto coin that stays close to the value of regular money like dollars.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Token', 'A digital item used in a blockchain, kind of like points in a video game.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Transaction', 'When someone sends or receives crypto.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Trading Pair', 'Two cryptos that can be swapped with each other.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Uniswap', 'A place where people trade tokens directly without a company in charge.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Utility Token', 'A token used to unlock features in a crypto app.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Uptime', 'How often a blockchain or service is working without crashing.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Validator', 'Someone who checks if crypto transactions are legit.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Volatility', 'How much the price of crypto goes up and down.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Vesting Period', 'Time you must wait before you can use your crypto rewards.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Wallet', 'A tool to keep and use your crypto, like a digital purse.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Web3', 'The next version of the internet, built with crypto and blockchains.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Whale', 'Someone who owns a huge amount of crypto.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('XRP', 'The token used by Ripple to send money fast across the world.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('X (Twitter)', 'A social media site where lots of crypto news is shared.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('xDai', 'A fast and cheap blockchain to use for sending crypto.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Yield Farming', 'Putting your crypto to work to earn more coins.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('YTD', 'Year-to-Date — how something has performed since January.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('YFII', 'A special version of a crypto app used for earning yield.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Zero Knowledge Proof', 'A clever way to prove something is true without saying the details.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Zcash', 'A crypto that keeps your transactions more private.') ON CONFLICT (term) DO NOTHING;
INSERT INTO terms (term, meaning) VALUES ('Zk-Rollup', 'A way to make Ethereum faster and cheaper using smart math.') ON CONFLICT (term) DO NOTHING;
